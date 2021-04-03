package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.option.OptionName;
import io.vlingo.xoom.starter.task.option.OptionValue;
import io.vlingo.xoom.starter.terminal.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Agent.TERMINAL;
import static io.vlingo.xoom.starter.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.starter.task.Property.DOCKER_REPOSITORY;

public class DockerPushCommandResolverStepTest {

    private static final String EXPECTED_COMMAND = "cd /home/projects/xoom-app && docker tag xoom-app:1.0.0 vlingo/xoom-app:latest && docker push vlingo/xoom-app:latest";

    @Test
    public void testDockerPackageCommandResolution() {
        Terminal.enable(Terminal.MAC_OS);

        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "1.0.0:latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");
        properties.put(DOCKER_REPOSITORY.literal(), "vlingo/xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        new DockerPushCommandResolverStep().process(context);

        Assertions.assertEquals(Terminal.supported().initializationCommand(), context.commands()[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), context.commands()[1]);
        Assertions.assertEquals(EXPECTED_COMMAND, context.commands()[2]);
    }

    @Test
    public void testDockerPackageCommandResolutionWithoutDockerProperties() {
        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "1.0.0:latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final Properties properties = new Properties();

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        Assertions.assertThrows(DockerCommandException.class, () -> {
            new DockerPushCommandResolverStep().process(context);
        });
    }

    @AfterEach
    public void clear() {
        Terminal.disable();
    }

}
