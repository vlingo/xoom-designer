package io.vlingo.xoom.starter.task.docker;

import io.vlingo.xoom.starter.task.TaskExecutionException;

public class DockerCommandException extends TaskExecutionException {

    public DockerCommandException(final String message) {
        super(message);
    }

    public DockerCommandException(final Exception exception) {
        super(exception);
    }
}
