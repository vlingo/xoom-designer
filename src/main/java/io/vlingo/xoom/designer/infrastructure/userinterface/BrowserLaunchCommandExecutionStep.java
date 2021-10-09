// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.designer.infrastructure.UserInterface;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.Terminal;

public class BrowserLaunchCommandExecutionStep extends CommandExecutor implements TaskExecutionStep {

  public BrowserLaunchCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
      final String browserLaunchCommand = Terminal.supported().browserLaunchCommand();
      return String.format("%s %s", browserLaunchCommand, UserInterface.rootContext());
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return Configuration.resolveEnvironment().isLocal() && !Profile.isTestProfileEnabled();
  }
}
