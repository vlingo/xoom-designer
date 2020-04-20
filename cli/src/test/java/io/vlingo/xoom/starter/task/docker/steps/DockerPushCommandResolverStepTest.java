package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.option.OptionName;
import io.vlingo.xoom.starter.task.option.OptionValue;
import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.starter.task.Property.DOCKER_REPOSITORY;

public class DockerPushCommandResolverStepTest {

    private static final String EXPECTED_COMMAND = "cd /home/projects/xoom-app && docker tag xoom-app:1.0.0 vlingo/xoom-app:latest && docker push vlingo/xoom-app";

    @Test
    public void testDockerPackageCommandResolution() {
        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "1.0.0:latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");
        properties.put(DOCKER_REPOSITORY.literal(), "vlingo/xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        new DockerPushCommandResolverStep().process(context);

        Assert.assertEquals(Terminal.active().initializationCommand(), context.commands()[0]);
        Assert.assertEquals(Terminal.active().parameter(), context.commands()[1]);
        Assert.assertEquals(EXPECTED_COMMAND, context.commands()[2]);
    }

    @Test(expected = DockerCommandException.class)
    public void testDockerPackageCommandResolutionWithoutDockerProperties() {
        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "1.0.0:latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final Properties properties = new Properties();

        final TaskExecutionContext context =
                TaskExecutionContext.withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        new DockerPushCommandResolverStep().process(context);
    }

}
