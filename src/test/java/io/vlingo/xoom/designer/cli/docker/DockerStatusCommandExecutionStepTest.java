package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.cli.task.docker.DockerCommandException;
import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class DockerStatusCommandExecutionStepTest {

  private static final String EXPECTED_COMMAND = "docker ps --filter ancestor=xoom-app";

  @Test
  public void testDockerStatusCommandResolution() {
    final Properties properties = new Properties();
    properties.put(XoomTurboProperties.DOCKER_IMAGE, "xoom-app");

    final TaskExecutionContext context =
            TaskExecutionContext.bare();

    context.onProperties(properties);

    final CommandRetainer commandRetainer = new CommandRetainer();

   // new DockerStatusCommandExecutionStep(commandRetainer).processTaskWith(context);

    final String[] commandsSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandsSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandsSequence[1]);
    Assertions.assertEquals(EXPECTED_COMMAND, commandsSequence[2]);
  }

  @Test
  public void testDockerStatusCommandResolutionWithoutImage() {
    final TaskExecutionContext context =
            TaskExecutionContext.bare().onProperties(new Properties());

    Assertions.assertThrows(DockerCommandException.class, () -> {
      //new DockerStatusCommandExecutionStep(new CommandRetainer()).processTaskWith(context);
    });
  }
}
