package io.pinger.plus.spigot.gui.template.impl;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.spigot.gui.template.GuiTemplate;
import io.pinger.plus.spigot.gui.template.GuiTemplateService;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiTemplateServiceImpl implements GuiTemplateService {
    private final Map<String, GuiTemplate> templateMap = new HashMap<>();

    @Override
    public void createDefaultTemplates(Class<?> templateClass) {
        this.loadTemplatesByClass(templateClass).forEach(this::saveToPath);
    }

    @Override
    public void loadTemplates(Class<?> templateClass) {
        this.clearTemplates();

        final Set<String> templates = this.loadTemplatesByClass(templateClass)
                .stream()
                .map(GuiTemplate::getIdentifier)
                .collect(Collectors.toSet());

        for (final String identifier : templates) {
            final GuiTemplate loaded = this.getTemplateFromPath(identifier);
            this.templateMap.put(identifier, loaded);
        }
    }

    @Override
    public void clearTemplates() {
        this.templateMap.clear();
    }

    @Override
    public GuiTemplate getTemplate(String templateIdentifier) {
        return this.templateMap.get(templateIdentifier);
    }

    private GuiTemplate getTemplateFromPath(String path) {
        final Path file = this.resolvePath(path);
        if (!Files.exists(file)) {
            throw new IllegalStateException("Failed to find file with name " + path);
        }

        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file.toFile());
        return new GuiTemplate(configuration.getValues(false));
    }

    @SneakyThrows
    private Set<GuiTemplate> loadTemplatesByClass(Class<?> clazz) {
        final Set<GuiTemplate> templates = new HashSet<>();
        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            final int params = method.getParameterCount();
            if (params == 0) {
                continue;
            }

            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (!GuiTemplate.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }

            final GuiTemplate template = (GuiTemplate) method.invoke(null);
            templates.add(template);
        }

        return templates;
    }

    @SneakyThrows
    private void saveToPath(GuiTemplate template, String path) {
        final Path file = this.resolvePath(path);
        if (!Files.exists(file)) {
            return;
        }

        final YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("", template.serialize());
        configuration.save(file.toFile());
    }

    @SneakyThrows
    private void saveToPath(GuiTemplate template) {
        this.saveToPath(template, String.format("%s.yml", template.getIdentifier()));
    }

    @SneakyThrows
    private Path resolvePath(String path) {
        final Plugin plugin = Instances.get(Plugin.class);
        if (plugin == null) {
            throw new IllegalStateException("Failed to get the plugin instance");
        }

        final Path pluginsPath = plugin.getDataFolder().toPath();
        final Path inventoriesPath = pluginsPath.resolve("inventories");
        if (!Files.exists(inventoriesPath)) {
            Files.createDirectories(inventoriesPath);
        }

        return inventoriesPath.resolve(path);
    }

}
