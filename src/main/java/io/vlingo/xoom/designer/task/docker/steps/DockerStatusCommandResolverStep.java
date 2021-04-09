package io.vlingo.xoom.designer.task.docker.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.docker.DockerCommandException;
import io.vlingo.xoom.designer.task.steps.CommandResolverStep;

import static io.vlingo.xoom.designer.task.Property.DOCKER_IMAGE;

public class DockerStatusCommandResolverStep extends CommandResolverStep {

    private static final String COMMAND_PATTERN = "docker ps --filter ancestor=%s";

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String image = context.propertyOf(DOCKER_IMAGE);
        if(image == null) {
            throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
        }
        return String.format(COMMAND_PATTERN, image);
    }
}
