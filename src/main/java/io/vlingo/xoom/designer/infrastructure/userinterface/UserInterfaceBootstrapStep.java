package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionException;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;
import io.vlingo.xoom.designer.cli.TaskExecutor;

public class UserInterfaceBootstrapStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            TaskExecutor.skipAutomaticExit();
            XoomInitializer.main(new String[]{});
        } catch (final Exception exception) {
            throw new TaskExecutionException(exception);
        }
    }

}
