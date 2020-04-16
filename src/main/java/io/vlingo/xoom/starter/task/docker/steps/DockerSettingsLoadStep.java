package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.option.OptionName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class DockerSettingsLoadStep implements TaskExecutionStep {

    private final static String DOCKER_SETTINGS_FILE = "vlingo-xoom.properties";

    @Override
    public void process(final TaskExecutionContext context) {
        context.onProperties(loadProperties(context));
    }

    private Properties loadProperties(final TaskExecutionContext context) {
        try {
            final Properties properties = new Properties();

            final String currentDirectory =
                    context.optionValueOf(OptionName.CURRENT_DIRECTORY);

            final String path =
                    Paths.get(currentDirectory, DOCKER_SETTINGS_FILE).toString();

            final File propertiesFile = new File(path);
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final FileNotFoundException e) {
            throw new DockerCommandException("Cannot execute Docker commands: unable to find " + DOCKER_SETTINGS_FILE);
        } catch (final IOException e) {
            throw new DockerCommandException(e);
        }
    }
}
