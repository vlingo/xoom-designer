package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;

public abstract class CommandResolverStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.withCommands(new String[]{
                Terminal.active().initializationCommand(),
                Terminal.active().parameter(),
                formatCommands(context)});
    }

    protected abstract String formatCommands(final TaskExecutionContext context);
}
