package io.vlingo.xoom.designer.task.docker;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.docker.DockerCommandException;
import io.vlingo.xoom.designer.task.docker.DockerPushCommandExecutionStep;
import io.vlingo.xoom.designer.task.OptionName;
import io.vlingo.xoom.designer.task.OptionValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

import static io.vlingo.xoom.designer.task.Agent.TERMINAL;
import static io.vlingo.xoom.designer.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.designer.task.Property.DOCKER_REPOSITORY;

public class DockerPushCommandExecutionStepTest {

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

        final CommandRetainer commandRetainer = new CommandRetainer();

        new DockerPushCommandExecutionStep(commandRetainer).process(context);

        final String[] commandsSequence = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commandsSequence[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commandsSequence[1]);
        Assertions.assertEquals(EXPECTED_COMMAND, commandsSequence[2]);
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
            new DockerPushCommandExecutionStep(new CommandRetainer()).process(context);
        });
    }

    @AfterEach
    public void clear() {
        Terminal.disable();
    }

}
