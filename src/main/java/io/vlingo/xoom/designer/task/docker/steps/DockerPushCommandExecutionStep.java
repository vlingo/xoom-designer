package io.vlingo.xoom.designer.task.docker.steps;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.docker.DockerCommandException;
import io.vlingo.xoom.designer.task.steps.CommandExecutionStep;

import static io.vlingo.xoom.designer.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.designer.task.Property.DOCKER_REPOSITORY;
import static io.vlingo.xoom.designer.task.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.designer.task.option.OptionName.TAG;

public class DockerPushCommandExecutionStep extends CommandExecutionStep {

    private static final String LOCAL = "LOCAL";
    private static final String REMOTE = "REMOTE";
    private static final String COMMAND_PATTERN = "%s && docker tag %s:%s %s:%s && docker push %s:%s";

  public DockerPushCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String image = context.propertyOf(DOCKER_IMAGE);
        final String repo = context.propertyOf(DOCKER_REPOSITORY);
        final String projectDirectoryCommand =
                Terminal.supported().resolveDirectoryChangeCommand(context.optionValueOf(CURRENT_DIRECTORY));

        if(image == null || repo == null) {
            throw new DockerCommandException("Please set Docker properties in vlingo-xoom.properties");
        }

        return String.format(COMMAND_PATTERN, projectDirectoryCommand, image,
                getTag(context, LOCAL), repo, getTag(context, REMOTE),
                repo, getTag(context, REMOTE));
    }

    private String getTag(final TaskExecutionContext context, final String type) {
        final String tag = context.optionValueOf(TAG);
        if(!tag.contains(":")) {
            return tag;
        }
        final String[] tags = tag.split(":");
        return type.equals(LOCAL) ? tags[0] : tags[1];
    }

}
