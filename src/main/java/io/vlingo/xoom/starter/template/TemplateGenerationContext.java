package io.vlingo.xoom.starter.template;

import io.vlingo.xoom.starter.archetype.ArchetypeProperties;

import java.util.Properties;

public class TemplateGenerationContext {

    private Process process;
    private Properties properties;

    public void followProcess(final Process process) {
        this.process = process;
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

    public String propertyOf(final ArchetypeProperties key) {
        return properties != null ? properties.getProperty(key.literal()) : "";
    }
}
