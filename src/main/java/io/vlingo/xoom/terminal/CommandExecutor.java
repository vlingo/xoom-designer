// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.terminal;

import io.vlingo.xoom.actors.Logger;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class CommandExecutor{

  private final Logger logger = Logger.basicLogger();
  private final CommandExecutionProcess commandExecutionProcess;

  protected CommandExecutor(final CommandExecutionProcess commandExecutionProcess) {
    this.commandExecutionProcess = commandExecutionProcess;
  }

  public void execute() {
    grantPermissions();
    logPreliminaryMessage();
    logCommandExecutionMessage();
    try {
      commandExecutionProcess.handle(formatCommands());
    } catch (final CommandExecutionException exception) {
      throw resolveCommandExecutionException(exception);
    }
  }

  private void logCommandExecutionMessage() {
    final String message = "Executing commands from " + this.getClass().getCanonicalName();
    logger.info(message);
  }

  protected abstract String formatCommands();

  protected List<File> executableFiles() {
    return Collections.emptyList();
  }

  protected void grantPermissions() {
    executableFiles().forEach(Terminal::grantAllPermissions);
  }

  protected CommandExecutionException resolveCommandExecutionException(final CommandExecutionException exception) {
    return exception;
  }

  protected void logPreliminaryMessage() {
  }

  protected Logger logger() {
    return logger;
  }

  public CommandExecutionProcess commandExecutionProcess() {
    return commandExecutionProcess;
  }

}
