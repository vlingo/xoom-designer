// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.steps;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class CommandExecutionStep implements TaskExecutionStep {

  private final CommandExecutionProcess commandExecutionProcess;

  protected CommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    this.commandExecutionProcess = commandExecutionProcess;
  }

  public void process(final TaskExecutionContext context) {
    grantPermissions();
    System.out.println("Executing commands from " + this.getClass().getCanonicalName());
    commandExecutionProcess.handle(formatCommands(context));
  }

  protected abstract String formatCommands(final TaskExecutionContext context);

  protected List<File> executableFiles() {
    return Collections.emptyList();
  }

  protected void grantPermissions() {
    executableFiles().forEach(Terminal::grantAllPermissions);
  }

}
