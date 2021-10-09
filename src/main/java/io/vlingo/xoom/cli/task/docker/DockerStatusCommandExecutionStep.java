package io.vlingo.xoom.cli.task.docker;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.designer.infrastructure.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;

public class DockerStatusCommandExecutionStep extends CommandExecutor implements TaskExecutionStep {

  private static final String COMMAND_PATTERN = "docker ps --filter ancestor=%s";

  public DockerStatusCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final String image = context.propertyOf(XoomTurboProperties.DOCKER_IMAGE);
    if (image == null) {
      throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
    }
    return String.format(COMMAND_PATTERN, image);
  }
}
