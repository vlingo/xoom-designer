// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.k8s.steps;

import io.vlingo.xoom.starter.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;

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
