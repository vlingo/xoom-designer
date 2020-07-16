package io.vlingo.xoom.starter.task.gui.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserInterfaceBootstrapStepTest {

    @Test
    public void testCommandManagement() {
        new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
        Assertions.assertFalse(TaskExecutor.shouldExit());
    }


}
