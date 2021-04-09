package io.vlingo.xoom.designer.task.projectgeneration.gui.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutionException;
import io.vlingo.xoom.designer.task.TaskExecutor;
import io.vlingo.xoom.designer.task.projectgeneration.gui.XoomInitializer;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

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
