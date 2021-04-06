// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.starter.infrastructure.terminal.Terminal;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;

public class AngularSchematicExecutionStep extends CommandExecutionStep {

  public AngularSchematicExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final String temporaryProjectPath =
            context.temporaryProjectPath();

    final String projectFolderCommand =
            Terminal.supported().resolveDirectoryChangeCommand(temporaryProjectPath);

    return projectFolderCommand + " && ng schematic";
  }

}
