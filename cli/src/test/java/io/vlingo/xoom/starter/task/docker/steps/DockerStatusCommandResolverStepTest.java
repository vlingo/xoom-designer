package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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

        Assertions.assertEquals(Terminal.supported().initializationCommand(), context.commands()[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), context.commands()[1]);
        Assertions.assertEquals(EXPECT_COMMAND, context.commands()[2]);
    }

    @Test
    public void testDockerStatusCommandResolutionWithoutImage() {
        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(new Properties());

        Assertions.assertThrows(DockerCommandException.class, () -> {
            new DockerStatusCommandResolverStep().process(context);
        });
    }
}
