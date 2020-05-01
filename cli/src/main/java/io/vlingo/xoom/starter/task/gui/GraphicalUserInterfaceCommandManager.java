package io.vlingo.xoom.starter.task.gui;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskManager;
import io.vlingo.xoom.starter.task.gui.steps.GraphicalUserInterfaceBootstrapStep;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;
import io.vlingo.xoom.starter.task.steps.LoggingStep;
import io.vlingo.xoom.starter.task.steps.StatusHandlingStep;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.util.Arrays;
import java.util.List;

public class GraphicalUserInterfaceCommandManager implements TaskManager {

    @Override
    public void run(final List<String> args) {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        STEPS.forEach(step -> step.process(context));
    }

    private static final List<TaskExecutionStep> STEPS = Arrays.asList(
            new GraphicalUserInterfaceBootstrapStep(), new BrowserLaunchCommandResolverStep(),
            new CommandExecutionStep(), new LoggingStep(), new StatusHandlingStep()
    );
}
