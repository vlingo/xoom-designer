// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.gui.steps;

import io.vlingo.xoom.starter.ApplicationConfiguration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.template.Terminal;

import java.util.HashMap;
import java.util.Map;

import static io.vlingo.xoom.starter.ApplicationConfiguration.USER_INTERFACE;

public class BrowserLaunchCommandResolverStep extends CommandResolverStep {

    private static final Map<Terminal, String> BROWSER_LAUNCH_COMMAND = new HashMap<Terminal, String>() {{
        put(Terminal.WINDOWS, "start ");
        put(Terminal.MAC_OS, "open ");
        put(Terminal.LINUX, "xdg-open ");
    }};

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        return BROWSER_LAUNCH_COMMAND.get(Terminal.supported())
                + context.configurationOf(USER_INTERFACE);
    }

}
