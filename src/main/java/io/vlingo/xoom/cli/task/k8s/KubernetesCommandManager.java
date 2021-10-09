// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.k8s;

import io.vlingo.xoom.cli.task.SubTask;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskManager;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.cli.task.Task.K8S;

public class KubernetesCommandManager implements TaskManager<List<String>> {

  private static final int SUB_TASK_INDEX = 1;

  @Override
  public void run(final List<String> args) {
    final String command = args.get(SUB_TASK_INDEX);
    final SubTask subTask = K8S.subTaskOf(command);

    final TaskExecutionContext context =
            TaskExecutionContext.bare();

    Arrays.asList(subTask.commandResolverStep())
            .forEach(step -> step.processTaskWith(context));
  }

}
