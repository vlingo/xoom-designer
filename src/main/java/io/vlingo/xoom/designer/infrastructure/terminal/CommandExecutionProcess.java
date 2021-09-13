// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.terminal;

public abstract class CommandExecutionProcess {

  private final Terminal terminal;

  public CommandExecutionProcess() {
    this.terminal = Terminal.supported();
  }

  public void handle(final String command) {
      execute(terminal.prepareCommand(command));
      log();
      handleCommandExecutionStatus();
  }

  protected abstract void execute(final String[] commandSequence);

  protected abstract void log();

  protected abstract void handleCommandExecutionStatus();

}
