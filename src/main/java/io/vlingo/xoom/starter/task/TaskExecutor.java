package io.vlingo.xoom.starter.task;

import java.util.List;

public class TaskExecutor {

    private static final int MAIN_COMMAND_INDEX = 0;

    public static void execute(final List<String> args) {
        validateArgs(args);
        Task.trigger(args.get(MAIN_COMMAND_INDEX)).run(args);
    }

    private static void validateArgs(final List<String> args){
        if(args.size() == 0) {
            throw new CommandNotFoundException();
        }
    }

}
