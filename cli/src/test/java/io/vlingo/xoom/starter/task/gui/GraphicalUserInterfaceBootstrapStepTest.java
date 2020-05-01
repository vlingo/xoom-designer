package io.vlingo.xoom.starter.task.gui;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutor;
import io.vlingo.xoom.starter.task.gui.steps.GraphicalUserInterfaceBootstrapStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphicalUserInterfaceBootstrapStepTest {

    @Test
    public void testCommandManagement() {
        new GraphicalUserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
        Assertions.assertFalse(TaskExecutor.shouldExit());
    }


}
