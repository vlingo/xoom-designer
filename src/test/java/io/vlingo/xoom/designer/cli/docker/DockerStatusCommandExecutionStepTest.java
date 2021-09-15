package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.designer.cli.Property.DOCKER_IMAGE;

public class DockerStatusCommandExecutionStepTest {

  private static final String EXPECTED_COMMAND = "docker ps --filter ancestor=xoom-app";

  @Test
  public void testDockerStatusCommandResolution() {
    final Properties properties = new Properties();
    properties.put(DOCKER_IMAGE.literal(), "xoom-app");

    final TaskExecutionContext context =
            TaskExecutionContext.bare();

    context.onProperties(properties);

    final CommandRetainer commandRetainer = new CommandRetainer();

    new DockerStatusCommandExecutionStep(commandRetainer).process(context);

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
      new DockerStatusCommandExecutionStep(new CommandRetainer()).process(context);
    });
  }
}
