// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.gui.steps;

import io.vlingo.xoom.starter.infrastructure.Infrastructure;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;
import io.vlingo.xoom.starter.terminal.CommandExecutionProcess;
import io.vlingo.xoom.starter.terminal.Terminal;

public class BrowserLaunchCommandResolverStep extends CommandExecutionStep {

  public BrowserLaunchCommandResolverStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String browserLaunchCommand = Terminal.supported().browserLaunchCommand();
        return String.format("%s %s", browserLaunchCommand, Infrastructure.UserInterface.rootContext());
    }

}
