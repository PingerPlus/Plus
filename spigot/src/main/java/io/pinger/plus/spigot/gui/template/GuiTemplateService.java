package io.pinger.plus.spigot.gui.template;

import io.pinger.plus.instance.Instances;

public interface GuiTemplateService {

    static GuiTemplateService get() {
        return Instances.get(GuiTemplateService.class);
    }

    void createDefaultTemplates(Class<?> templateClass);

    void loadTemplates(Class<?> templateClass);

    void clearTemplates();

    GuiTemplate getTemplate(String templateIdentifier);

    default GuiTemplate getTemplate(GuiTemplate baseTemplate) {
        return this.getTemplate(baseTemplate.getIdentifier());
    }

}
