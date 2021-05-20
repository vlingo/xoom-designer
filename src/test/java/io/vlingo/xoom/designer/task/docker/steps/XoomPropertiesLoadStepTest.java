package io.vlingo.xoom.designer.task.docker.steps;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.ResourceLoadException;
import io.vlingo.xoom.designer.task.Property;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.option.OptionValue;
import io.vlingo.xoom.designer.task.steps.XoomPropertiesLoadStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;

import static io.vlingo.xoom.designer.task.Agent.TERMINAL;
import static io.vlingo.xoom.designer.task.option.OptionName.CURRENT_DIRECTORY;

public class XoomPropertiesLoadStepTest {

    @Test
    public void testDockerSettingsLoad() {
        final String propertiesAbsolutePath =
                Paths.get(System.getProperty("user.dir")).toString();

        final OptionValue currentDirectory =
                OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .withOptions(Arrays.asList(currentDirectory));

        new XoomPropertiesLoadStep().process(context);

        Assertions.assertEquals("xoom-app", context.propertyOf(Property.DOCKER_IMAGE));
        Assertions.assertEquals("vlingo/xoom-app", context.propertyOf(Property.DOCKER_REPOSITORY));
    }

    @Test
    public void testMissingDockerSettings() {
        final String propertiesAbsolutePath =
                Paths.get(System.getProperty("user.dir"), "invalid-directory").toString();

        final OptionValue currentDirectory =
                OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .withOptions(Arrays.asList(currentDirectory));

        Assertions.assertThrows(ResourceLoadException.class, () -> {
            new XoomPropertiesLoadStep().process(context);
        });
    }

    @BeforeEach
    public void setUp() {
        Profile.enableTestProfile();
        Infrastructure.clear();
    }

    @BeforeEach
    public void clear() {
        Profile.disableTestProfile();
    }
}
