package io.vlingo.xoom.starter.task.gui.steps;

import io.micronaut.runtime.Micronaut;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutor;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

public class GraphicalUserInterfaceBootstrapStep implements TaskExecutionStep {

    @Override
    public void process(TaskExecutionContext context) {
        Micronaut.run(GraphicalUserInterfaceBootstrapStep.class);
        TaskExecutor.skipAutomaticExit();
    }

}
