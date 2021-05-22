// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.k8s;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.CommandExecutionStep;

import java.nio.file.Paths;

public class KubernetesPushCommandExecutionStep extends CommandExecutionStep {

  private static final String COMMAND_PATTERN = "kubectl apply -f %s";

  public KubernetesPushCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    return String.format(COMMAND_PATTERN, Paths.get("deployment", "k8s"));
  }

}
