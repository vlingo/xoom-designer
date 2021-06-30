// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

public class BrowserLaunchCommandExecutionStep extends CommandExecutionStep {

  public BrowserLaunchCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
      final String browserLaunchCommand = Terminal.supported().browserLaunchCommand();
      return String.format("%s %s", browserLaunchCommand, Infrastructure.UserInterface.rootContext());
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return Configuration.resolveEnvironment().isLocal();
  }
}
