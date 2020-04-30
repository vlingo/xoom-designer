package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutionException;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.option.OptionName;
import io.vlingo.xoom.starter.task.option.OptionValue;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;

import static io.vlingo.xoom.starter.task.option.OptionName.CURRENT_DIRECTORY;

public class DockerSettingsLoadStepTest {

    @Test
    public void testDockerSettingsLoad() {
        final String propertiesAbsolutePath =
                Paths.get(System.getProperty("user.dir"), "src", "test", "resources").toString();

        final OptionValue currentDirectory =
                OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

        final TaskExecutionContext context =
                TaskExecutionContext.withOptions(Arrays.asList(currentDirectory));

        new DockerSettingsLoadStep().process(context);

        Assert.assertEquals("xoom-app", context.propertyOf(Property.DOCKER_IMAGE));
        Assert.assertEquals("vlingo/xoom-app", context.propertyOf(Property.DOCKER_REPOSITORY));
    }

    @Test(expected = DockerCommandException.class)
    public void testMissingDockerSettings() {
        final String propertiesAbsolutePath =
                Paths.get(System.getProperty("user.dir")).toString();

        final OptionValue currentDirectory =
                OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

        final TaskExecutionContext context =
                TaskExecutionContext.withOptions(Arrays.asList(currentDirectory));

        new DockerSettingsLoadStep().process(context);
    }
}
