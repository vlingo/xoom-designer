package io.vlingo.xoom.starter.task.gui.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.template.Terminal;

import java.util.HashMap;
import java.util.Map;

public class BrowserLaunchCommandResolverStep extends CommandResolverStep {

    private static final String URL = "http://localhost:8080/xoom-starter";

    private static final Map<Terminal, String> BROWSER_LAUNCH_COMMAND = new HashMap<Terminal, String>() {{
        put(Terminal.WINDOWS, "start " + URL);
        put(Terminal.MAC_OS, "open " + URL);
        put(Terminal.LINUX, "xdg-open " + URL);
    }};

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        return BROWSER_LAUNCH_COMMAND.get(Terminal.supported());
    }

}
