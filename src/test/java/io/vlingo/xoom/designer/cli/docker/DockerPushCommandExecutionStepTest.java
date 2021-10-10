package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.XoomTurboProperties;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

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
        properties.put(XoomTurboProperties.DOCKER_IMAGE, "xoom-app");
        properties.put(XoomTurboProperties.DOCKER_REPOSITORY, "vlingo/xoom-app");

        final TaskExecutionContext context =
                TaskExecutionContext.withOptions(Arrays.asList(tag, directory));

        context.onProperties(properties);

        final CommandRetainer commandRetainer = new CommandRetainer();

//        new DockerPushCommandExecutionStep(commandRetainer).processTaskWith(context);
//
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
                TaskExecutionContext.withOptions(Arrays.asList(tag, directory))
                        .onProperties(properties);

        //Assertions.assertThrows(DockerCommandException.class, () -> new DockerPushCommandExecutionStep(new CommandRetainer()).processTaskWith(context));
    }

    @AfterEach
    public void clear() {
        Terminal.disable();
    }

}
