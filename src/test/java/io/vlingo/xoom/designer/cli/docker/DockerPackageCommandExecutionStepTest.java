package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.designer.cli.Agent;
import io.vlingo.xoom.designer.cli.OptionName;
import io.vlingo.xoom.designer.cli.OptionValue;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

import static io.vlingo.xoom.designer.cli.Property.DOCKER_IMAGE;

public class DockerPackageCommandExecutionStepTest {

    private static final String EXPECTED_COMMAND = "cd /home/projects/xoom-app && mvn clean package && docker build ./ -t xoom-app:latest";

    @Test
    public void testDockerPackageCommandResolution() {
        Terminal.enable(Terminal.MAC_OS);

        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final Properties properties = new Properties();
        properties.put(DOCKER_IMAGE.literal(), "xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(Agent.TERMINAL).withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        final CommandRetainer commandRetainer = new CommandRetainer();

        new DockerPackageCommandExecutionStep(commandRetainer).process(context);

        final String[] commandsSequence = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commandsSequence[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commandsSequence[1]);
        Assertions.assertEquals(EXPECTED_COMMAND, commandsSequence[2]);
    }

    @Test
    public void testDockerPackageCommandResolutionWithoutImage() {
        final OptionValue tag =
                OptionValue.with(OptionName.TAG, "latest");

        final OptionValue directory =
                OptionValue.with(OptionName.CURRENT_DIRECTORY, "/home/projects/xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(Agent.TERMINAL)
                        .withOptions(Arrays.asList(tag, directory))
                        .onProperties(new Properties());

        Assertions.assertThrows(DockerCommandException.class, () ->{
            new DockerPackageCommandExecutionStep(new CommandRetainer()).process(context);
        });
    }

    @AfterEach
    public void clear() {
        Terminal.disable();
    }
}
