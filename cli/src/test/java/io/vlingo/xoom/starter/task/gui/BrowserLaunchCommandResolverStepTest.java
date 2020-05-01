package io.vlingo.xoom.starter.task.gui;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BrowserLaunchCommandResolverStepTest {

    private static final String EXPECTED_URL = "http://localhost:8080/xoom-starter";

    @Test
    public void testBrowserLaunchCommandResolution() {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        new BrowserLaunchCommandResolverStep().process(context);
        final String[] commands = context.commands();
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

}
