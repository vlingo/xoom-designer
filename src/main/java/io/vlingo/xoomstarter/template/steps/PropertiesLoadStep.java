package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import io.vlingo.xoomstarter.template.TemplateGenerationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoadStep implements TemplateGenerationStep {

    private static final String ROOT_FOLDER_KEY = "user.dir";
    private static final String PROPERTIES_PATH_PATTERN = "%s%s%s";
    private static final String PROPERTIES_FILE_NAME = "vlingo-xoom-starter.properties";

    @Override
    public void process(final TemplateGenerationContext context) {
        context.onProperties(loadProperties());
    }

    private Properties loadProperties() {
        try {
            final Properties properties = new Properties();
            final String fullPath = resolvePropertiesPath();
            properties.load(new FileInputStream(new File(fullPath)));
            return properties;
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    private String resolvePropertiesPath() {
        return String.format(PROPERTIES_PATH_PATTERN,
                System.getProperty(ROOT_FOLDER_KEY),
                File.separator, PROPERTIES_FILE_NAME);
    }

}
