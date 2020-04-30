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

public class DockerStatusCommandResolverStepTest {

    private static final String EXPECT_COMMAND = "docker ps --filter ancestor=xoom-app";

    @Test
    public void testDockerStatusCommandResolution() {
        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(properties);

        new DockerStatusCommandResolverStep().process(context);

        Assert.assertEquals(Terminal.active().initializationCommand(), context.commands()[0]);
        Assert.assertEquals(Terminal.active().parameter(), context.commands()[1]);
        Assert.assertEquals(EXPECT_COMMAND, context.commands()[2]);
    }

    @Test(expected = DockerCommandException.class)
    public void testDockerStatusCommandResolutionWithoutImage() {
        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(new Properties());

        new DockerStatusCommandResolverStep().process(context);
    }
}
