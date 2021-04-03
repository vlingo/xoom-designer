package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;
import io.vlingo.xoom.starter.terminal.CommandExecutionProcess;

import static io.vlingo.xoom.starter.task.Property.DOCKER_IMAGE;

public class DockerStatusCommandResolverStep extends CommandExecutionStep {

    private static final String COMMAND_PATTERN = "docker ps --filter ancestor=%s";

    public DockerStatusCommandResolverStep(final CommandExecutionProcess commandExecutionProcess) {
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
