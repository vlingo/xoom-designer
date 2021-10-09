// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.terminal;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.terminal.CommandExecutionException;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.Terminal;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class CommandExecutor {

  private final CommandExecutionProcess commandExecutionProcess;

  protected CommandExecutor(final CommandExecutionProcess commandExecutionProcess) {
    this.commandExecutionProcess = commandExecutionProcess;
  }

  public void processTaskWith(final TaskExecutionContext context) {
    grantPermissions();
    logPreliminaryMessage(context);
    logCommandExecutionMessage(context);
    try {
      commandExecutionProcess.handle(formatCommands(context));
    } catch (final CommandExecutionException exception) {
      throw resolveCommandExecutionException(exception);
    }
  }

  private void logCommandExecutionMessage(final TaskExecutionContext context) {
    final String message = "Executing commands from " + this.getClass().getCanonicalName();
    if(context.logger() == null) {
      System.out.println(message);
    } else {
      context.logger().info(message);
    }
  }

  protected abstract String formatCommands(final TaskExecutionContext context);

  protected List<File> executableFiles() {
    return Collections.emptyList();
  }

  protected void grantPermissions() {
    executableFiles().forEach(Terminal::grantAllPermissions);
  }

  protected CommandExecutionException resolveCommandExecutionException(final CommandExecutionException exception) {
    return exception;
  }

  protected void logPreliminaryMessage(final TaskExecutionContext context) {
  }

  public CommandExecutionProcess commandExecutionProcess() {
    return commandExecutionProcess;
  }

}
