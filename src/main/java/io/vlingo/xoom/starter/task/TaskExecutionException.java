package io.vlingo.xoom.starter.task;

public class TaskExecutionException extends RuntimeException {

    public TaskExecutionException(final Exception exception) {
        super(exception);
    }

    public TaskExecutionException(final String message) {
        super(message);
    }

}
