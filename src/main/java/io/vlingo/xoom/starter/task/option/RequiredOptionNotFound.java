package io.vlingo.xoom.starter.task.option;

import io.vlingo.xoom.starter.task.TaskExecutionException;

public class RequiredOptionNotFound extends TaskExecutionException {

    public RequiredOptionNotFound(final String optionName) {
        super("The value is required for option " + optionName + " not found.");
    }

}
