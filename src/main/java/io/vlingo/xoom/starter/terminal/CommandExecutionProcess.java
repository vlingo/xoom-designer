// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.terminal;

public abstract class CommandExecutionProcess {

  public void handle(final String command) {
    try {
      final String[] commands =
              prepareCommandToTerminal(command);

      final Process process = execute(commands);

      log(process);
      handleCommandExecutionStatus(process.waitFor());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected String[] prepareCommandToTerminal(final String commands) {
    final Terminal terminal = Terminal.supported();
    return new String[]{terminal.initializationCommand(), terminal.parameter(), commands};
  }

  protected abstract Process execute(final String[] commands);

  protected abstract void log(final Process process);

  protected abstract void handleCommandExecutionStatus(final int commandExecutionStatus);
}
