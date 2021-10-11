// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.terminal;

public abstract class CommandExecutionProcess {

  private final Terminal terminal;

  public CommandExecutionProcess() {
    this.terminal = Terminal.supported();
  }

  public void handle(final String command) {
      final Process process = execute(terminal.prepareCommand(command));
      log(process);
      handleCommandExecutionStatus(process);
  }

  protected abstract Process execute(final String[] commandSequence);

  protected abstract void log(final Process process);

  protected abstract void handleCommandExecutionStatus(final Process process);

}
