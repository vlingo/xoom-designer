package io.vlingo.xoom.designer.task.docker.steps;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.docker.DockerCommandException;
import io.vlingo.xoom.designer.task.steps.CommandExecutionStep;

import static io.vlingo.xoom.designer.task.Property.DOCKER_IMAGE;

public class DockerStatusCommandExecutionStep extends CommandExecutionStep {

    private static final String COMMAND_PATTERN = "docker ps --filter ancestor=%s";

    public DockerStatusCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
        super(commandExecutionProcess);
    }

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String image = context.propertyOf(DOCKER_IMAGE);
        if(image == null) {
            throw new DockerCommandException("Please set the docker.image property in vlingo-xoom.properties");
        }
        return String.format(COMMAND_PATTERN, image);
    }
}
