// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.k8s;

import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.terminal.CommandExecutionProcess;

import java.nio.file.Paths;

public class KubernetesPushCommandExecutionStep extends CommandExecutor implements TaskExecutionStep {

  public KubernetesPushCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands() {
    return String.format("kubectl apply -f %s", Paths.get("deployment", "k8s"));
  }

  @Override
  public void processTaskWith(TaskExecutionContext context) {

  }
}
