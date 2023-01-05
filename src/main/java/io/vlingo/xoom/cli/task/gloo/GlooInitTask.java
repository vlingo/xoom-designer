// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;

import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;

public class GlooInitTask extends Task {

  private final CommandExecutionProcess commandExecutionProcess;

  public GlooInitTask(final CommandExecutionProcess commandExecutionProcess) {
    super("gloo init", Option.required(CURRENT_DIRECTORY));
    this.commandExecutionProcess = commandExecutionProcess;
  }

  @Override
  public void run(final List<String> args) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        return "glooctl install gateway";
      }
    }.execute();
  }

}
