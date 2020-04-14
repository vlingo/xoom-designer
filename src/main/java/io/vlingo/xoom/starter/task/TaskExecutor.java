package io.vlingo.xoom.starter.task;

public class TaskExecutor {

    private static final int MAIN_COMMAND_INDEX = 0;

    public static void execute(final String [] args) {
        try {
            validateArgs(args);
            Task.fromCommand(args[MAIN_COMMAND_INDEX]).execute(args);
        } catch (final TaskExecutionException exception) {
            exception.printStackTrace();
        }
    }

    private static void validateArgs(final String [] args){
        if(args.length == 0) {
            throw new CommandNotFoundException();
        }
    }
}
