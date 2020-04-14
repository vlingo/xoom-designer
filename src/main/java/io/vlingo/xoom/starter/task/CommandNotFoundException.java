package io.vlingo.xoom.starter.task;

public class CommandNotFoundException extends TaskExecutionException {

    public CommandNotFoundException() {
        super("Please inform a command.");
    }

    public CommandNotFoundException(final String message) {
        super(message);
    }

}
