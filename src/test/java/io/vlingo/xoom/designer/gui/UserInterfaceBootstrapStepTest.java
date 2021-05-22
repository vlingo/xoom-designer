package io.vlingo.xoom.designer.gui;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.gui.UserInterfaceBootstrapStep;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutor;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserInterfaceBootstrapStepTest {

    @Test
    public void testThatUserInterfaceBootstrapStepIsProcessed() {
        new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
        Assertions.assertFalse(TaskExecutor.shouldExit());
    }

    @BeforeEach
    public void setUp() {
        Infrastructure.clear();
        ComponentRegistry.clear();
        Profile.enableTestProfile();
        Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
        ComponentRegistry.register(GenerationTarget.class, GenerationTarget.FILESYSTEM);
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
    }
}
