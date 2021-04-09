package io.vlingo.xoom.designer.task.gui.steps;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.BrowserLaunchCommandExecutionStep;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrowserLaunchCommandExecutionStepTest {

    // "xoom-designer": This will not work until a resource for it is created.
    // private static final String EXPECTED_URL = "http://localhost:19090/xoom-designer";
    private static final String EXPECTED_URL = "http://localhost:19090/context";

    @Test
    public void testBrowserLaunchCommandResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        final CommandRetainer commandRetainer = new CommandRetainer();

        new BrowserLaunchCommandExecutionStep(commandRetainer).process(context);

        final String[] commands = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commands[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commands[1]);
        Assertions.assertEquals(expectedLaunchCommand(), commands[2]);
    }

    private String expectedLaunchCommand() {
        final Terminal supportedTerminal = Terminal.supported();
        if(supportedTerminal.equals(Terminal.WINDOWS)) {
            return "start " + EXPECTED_URL;
        }
        if(supportedTerminal.equals(Terminal.MAC_OS)) {
            return "open " + EXPECTED_URL;
        }
        if(supportedTerminal.equals(Terminal.LINUX)) {
            return "xdg-open " + EXPECTED_URL;
        }
        throw new RuntimeException("Terminal is not supported");
    }

    @BeforeEach
    public void setUp() {
        Terminal.disable();
        Infrastructure.clear();
        Profile.enableTestProfile();
        Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
    }
}
