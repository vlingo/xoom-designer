package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutionException;

import java.io.IOException;

public class CommandExecutionStep implements TaskExecutionStep {

    public void process(final TaskExecutionContext context) {
        try {
            context.followProcess(
                Runtime.getRuntime().exec(context.commands())
            );
        } catch (final IOException e) {
            throw new TaskExecutionException(e);
        }
    }
}
