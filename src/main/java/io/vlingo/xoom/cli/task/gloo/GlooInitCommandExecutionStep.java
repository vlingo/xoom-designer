// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.terminal.CommandExecutionProcess;

public class GlooInitCommandExecutionStep extends CommandExecutor implements TaskExecutionStep {

  public GlooInitCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(TaskExecutionContext context) {
    return "glooctl install gateway";
  }

}
