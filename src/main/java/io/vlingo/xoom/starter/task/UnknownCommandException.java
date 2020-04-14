package io.vlingo.xoom.starter.task;

public class UnknownCommandException extends TaskExecutionException {

    public UnknownCommandException(final String command) {
        super("The informed command is not supported " + command);
    }
}
