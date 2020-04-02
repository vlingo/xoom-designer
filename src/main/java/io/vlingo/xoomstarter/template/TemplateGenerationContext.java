package io.vlingo.xoomstarter.template;

import io.vlingo.xoomstarter.template.steps.PropertiesKeys;

import java.util.Properties;

public class TemplateGenerationContext {

    private Process process;
    private Properties properties;
    private Archetype archetype;

    public void followProcess(final Process process) {
        this.process = process;
    }

    public void selectArchetype(final Archetype archetype) {
        this.archetype = archetype;
    }

    public void onProperties(final Properties properties) {
        this.properties = properties;
    }

    public Process process() {
        return process;
    }

    public Properties properties() {
        return properties;
    }

    public Archetype archetype() {
        return archetype;
    }

    public String propertyOf(final PropertiesKeys key) {
        return properties != null ? properties.getProperty(key.literal()) : "";
    }
}
