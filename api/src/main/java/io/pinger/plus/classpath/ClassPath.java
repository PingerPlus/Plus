package io.pinger.plus.classpath;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.reflect.Reflection;
import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClassPath {
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = info -> info.className.indexOf('$') == -1;
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";

    private final Set<ResourceInfo> resources;

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        final DefaultScanner scanner = new DefaultScanner();
        scanner.scan(classloader);
        return new ClassPath(scanner.getResources());
    }

    public Set<ResourceInfo> getResources() {
        return this.resources;
    }

    public Set<ClassInfo> getAllClasses() {
        return this.resources
            .stream()
            .filter(ClassInfo.class::isInstance)
            .map(ClassInfo.class::cast)
            .collect(Collectors.toSet());
    }

    public Set<ClassInfo> getTopLevelClasses() {
        return this.resources
            .stream()
            .filter(ClassInfo.class::isInstance)
            .map(ClassInfo.class::cast)
            .filter(IS_TOP_LEVEL)
            .collect(Collectors.toSet());
    }

    public static class ResourceInfo {
        private final String resourceName;

        protected final ClassLoader loader;

        ResourceInfo(@NotNull String resourceName, @NotNull ClassLoader loader) {
            this.resourceName = resourceName;
            this.loader = loader;
        }

        public static ResourceInfo of(@NotNull String resourceName, @NotNull ClassLoader loader) {
            if (resourceName.endsWith(CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(resourceName, loader);
            }

            return new ResourceInfo(resourceName, loader);
        }

        /** Returns the fully qualified name of the resource. Such as "com/mycomp/foo/bar.txt". */
        public final String getResourceName() {
            return this.resourceName;
        }

        @Override
        public int hashCode() {
            return this.resourceName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ResourceInfo) {
                final ResourceInfo that = (ResourceInfo) obj;
                return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
            }
            return false;
        }

        @Override
        public String toString() {
            return this.resourceName;
        }
    }

    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        public ClassInfo(String resourceName, ClassLoader loader) {
            super(resourceName, loader);
            this.className = getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        /**
         * Returns the simple name of the underlying class as given in the source code.
         *
         * <p>Behaves identically to {@link Class#getSimpleName()} but does not require the class to be
         * loaded.
         */
        public String getSimpleName() {
            final int lastDollarSign = this.className.lastIndexOf('$');
            if (lastDollarSign != -1) {
                final String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.digit().trimLeadingFrom(innerClassName);
            }

            final String packageName = this.getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }

            // Since this is a top level class, its simple name is always the part after package name.
            return this.className.substring(packageName.length() + 1);
        }

        /**
         * Returns the fully qualified name of the class.
         *
         * <p>Behaves identically to {@link Class#getName()} but does not require the class to be
         * loaded.
         */
        public String getName() {
            return this.className;
        }

        /**
         * Loads (but doesn't link or initialize) the class.
         *
         * @throws LinkageError when there were errors in loading classes that this class depends on.
         *     For example, {@link NoClassDefFoundError}.
         */
        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                // Shouldn't happen, since the class name is read from the class path.
                throw new IllegalStateException(e);
            }
        }

        @Override
        public String toString() {
            return this.className;
        }
    }

    abstract static class Scanner {
        private final Set<File> scannedUris = Sets.newHashSet();

        public final void scan(ClassLoader loader) throws IOException {
            final Map<File, ClassLoader> entries = this.getClassPathEntries(loader);
            for (final Map.Entry<File, ClassLoader> entry : entries.entrySet()) {
                this.scan(entry.getKey(), entry.getValue());
            }
        }

        protected abstract void scanDirectory(ClassLoader loader, File directory) throws IOException;

        protected abstract void scanJarFile(ClassLoader loader, JarFile file) throws IOException;

        private void scan(File file, ClassLoader classloader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                this.scanFrom(file, classloader);
            }
        }

        private void scanFrom(File file, ClassLoader classLoader) throws IOException {
            if (file.isDirectory()) {
                this.scanDirectory(classLoader, file);
            } else {
                this.scanJar(file, classLoader);
            }
        }

        private void scanJar(File file, ClassLoader classLoader) {
            try (final JarFile jarFile = new JarFile(file)) {
                for (final File path : this.getClassPathFromManifest(file, jarFile.getManifest())) {
                    this.scan(path, classLoader);
                }

                this.scanJarFile(classLoader, jarFile);
            } catch (Exception e) {
                Instances.get(PluginLogger.class).info("Failed to scan jar {0}", file);
            }
        }

        private Set<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
            if (manifest == null) {
                return new HashSet<>();
            }

            final Set<File> files = new HashSet<>();
            final Attributes attributes = manifest.getMainAttributes();
            final String attribute = attributes.getValue(Attributes.Name.CLASS_PATH.toString());
            if (attribute == null) {
                return files;
            }

            for (final String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(attribute)) {
                try {
                    final URL url = this.getClassPathEntry(jarFile, path);
                    if (!url.getProtocol().equals("file")) {
                        continue;
                    }
                    files.add(new File(url.getFile()));
                } catch (MalformedURLException e) {
                    Instances.get(PluginLogger.class).info("Invalid Class-Path entry: {0}", path);
                }
            }
            return files;
        }

        private Map<File, ClassLoader> getClassPathEntries(ClassLoader loader) {
            if (loader == null) {
                return new HashMap<>();
            }

            final ClassLoader parent = loader.getParent();
            final Map<File, ClassLoader> entries = new HashMap<>(this.getClassPathEntries(parent));
            if (loader instanceof URLClassLoader) {
                final URLClassLoader urlLoader = (URLClassLoader) loader;
                for (final URL entry : urlLoader.getURLs()) {
                    if (!entry.getProtocol().equals("file")) {
                        continue;
                    }

                    final File file = new File(entry.getFile());
                    if (entries.containsKey(file)) {
                        continue;
                    }

                    entries.put(file, loader);
                }
            }
            return ImmutableMap.copyOf(entries);
        }

        private URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
            return new URL(jarFile.toURI().toURL(), path);
        }
    }

    public static final class DefaultScanner extends Scanner {
        private final SetMultimap<ClassLoader, String> resources = MultimapBuilder
            .hashKeys()
            .linkedHashSetValues()
            .build();

        ImmutableSet<ResourceInfo> getResources() {
            final ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
            for (final Map.Entry<ClassLoader, String> entry : this.resources.entries()) {
                builder.add(ResourceInfo.of(entry.getValue(), entry.getKey()));
            }
            return builder.build();
        }

        @Override
        protected void scanJarFile(ClassLoader classloader, JarFile file) {
            final Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME)) {
                    continue;
                }

                this.resources.get(classloader).add(entry.getName());
            }
        }

        @Override
        protected void scanDirectory(ClassLoader loader, File directory) {
            this.scanDirectory(directory, loader, "");
        }

        private void scanDirectory(File directory, ClassLoader loader, String packagePrefix) {
            final File[] files = directory.listFiles();
            if (files == null) {
                return;
            }

            for (final File f : files) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    this.scanDirectory(f, loader, packagePrefix + name + "/");
                } else {
                    final String resourceName = packagePrefix + name;
                    if (!resourceName.equals(JarFile.MANIFEST_NAME)) {
                        this.resources.get(loader).add(resourceName);
                    }
                }
            }
        }
    }

    private static String getClassName(String filename) {
        final int classNameEnd = filename.length() - CLASS_FILE_NAME_EXTENSION.length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
}