package io.pinger.plus.classpath;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import io.pinger.plus.asm.ClassProxy;
import io.pinger.plus.asm.InheritanceVisitor;
import io.pinger.plus.graph.Graph;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

public final class InheritanceGraph {
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";

    private final Graph<ClassProxy> inheritenceGraph;

    private InheritanceGraph(Graph<ClassProxy> inheritenceGraph) {
        this.inheritenceGraph = inheritenceGraph;
    }

    public static InheritanceGraph from(ClassLoader classloader)  {
        final DefaultScanner scanner = new DefaultScanner();
        scanner.scan(classloader);
        return new InheritanceGraph(scanner.getGraph());
    }

    public Graph<ClassProxy> getGraph() {
        return this.inheritenceGraph;
    }

    abstract static class Scanner {
        private final Set<File> scannedUris = Sets.newHashSet();

        public final void scan(ClassLoader loader) {
            try {
                final Map<File, ClassLoader> entries = this.getClassPathEntries(loader);
                for (final Map.Entry<File, ClassLoader> entry : entries.entrySet()) {
                    this.scan(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected abstract void scanDirectory(ClassLoader loader, File directory) throws IOException;

        protected abstract void scanJarFile(ClassLoader loader, JarFile file) throws IOException;

        private void scan(File file, ClassLoader classloader) throws IOException {
            final String decodedPath = URLDecoder.decode(file.getPath(), "UTF-8");
            final File decodedFile = new File(decodedPath);
            if (this.scannedUris.add(decodedFile.getCanonicalFile())) {
                this.scanFrom(decodedFile, classloader);
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
                e.printStackTrace();
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

                    final String decodedPath = URLDecoder.decode(url.getFile(), "UTF-8");
                    files.add(new File(decodedPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return files;
        }

        private Map<File, ClassLoader> getClassPathEntries(ClassLoader loader) {
            if (loader == null) {
                return new HashMap<>();
            }

            final Map<File, ClassLoader> entries = new HashMap<>();
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
        private final Graph<ClassProxy> graph = new Graph<>();

        Graph<ClassProxy> getGraph() {
            return this.graph;
        }

        @Override
        protected void scanJarFile(ClassLoader classloader, JarFile file) {
            final Enumeration<JarEntry> entries = file.entries();
            try {
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME)) {
                        continue;
                    }

                    if (entry.getName().endsWith(".class") && !entry.getName().startsWith("module-info")) {
                        try (final InputStream stream = file.getInputStream(entry)) {
                            final ClassReader reader = new ClassReader(stream);
                            reader.accept(new InheritanceVisitor(this.graph), 0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        @Override
        protected void scanDirectory(ClassLoader loader, File directory) {
            this.scanDirectory(directory, "");
        }

        @SneakyThrows
        private void scanDirectory(File directory, String packagePrefix) {
            final File[] files = directory.listFiles();
            if (files == null) {
                return;
            }

            for (final File f : files) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    this.scanDirectory(f, packagePrefix + name + "/");
                } else if (name.endsWith(CLASS_FILE_NAME_EXTENSION)) {
                    final ClassReader reader = new ClassReader(f.getPath());
                    reader.accept(new InheritanceVisitor(this.graph), 0);
                }
            }
        }
    }
}