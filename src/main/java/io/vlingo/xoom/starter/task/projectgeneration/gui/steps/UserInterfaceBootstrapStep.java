package io.vlingo.xoom.starter.task.projectgeneration.gui.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutionException;
import io.vlingo.xoom.starter.task.TaskExecutor;
import io.vlingo.xoom.starter.task.projectgeneration.gui.XoomInitializer;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

public class UserInterfaceBootstrapStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            XoomInitializer.main(new String[]{});
            TaskExecutor.skipAutomaticExit();
        } catch (final Exception exception) {
            throw new TaskExecutionException(exception);
        }
    }

}
