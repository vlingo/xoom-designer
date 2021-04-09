package io.vlingo.xoom.designer.task.gui.steps;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutor;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.UserInterfaceBootstrapStep;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserInterfaceBootstrapStepTest {

    @Test
    public void testCommandManagement() {
        new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
        Assertions.assertFalse(TaskExecutor.shouldExit());
    }

    @BeforeEach
    public void setUp() {
        Infrastructure.clear();
        Profile.enableTestProfile();
        Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
    }
}
