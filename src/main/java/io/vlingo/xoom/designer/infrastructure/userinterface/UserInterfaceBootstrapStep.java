package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionException;
import io.vlingo.xoom.cli.task.TaskExecutionStep;

public class UserInterfaceBootstrapStep implements TaskExecutionStep {

    @Override
    public void processTaskWith(final TaskExecutionContext context) {
        try {
            XoomInitializer.main(new String[]{});
        } catch (final Exception exception) {
            throw new TaskExecutionException(exception);
        }
    }

}
