// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.gui.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.projectgeneration.Terminal;

import static io.vlingo.xoom.starter.Configuration.BROWSER_LAUNCH_COMMAND;
import static io.vlingo.xoom.starter.Configuration.USER_INTERFACE_CONFIG_KEY;

public class BrowserLaunchCommandResolverStep extends CommandResolverStep {

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        return BROWSER_LAUNCH_COMMAND.get(Terminal.supported()) + context.configurationOf(USER_INTERFACE_CONFIG_KEY);
    }

}
