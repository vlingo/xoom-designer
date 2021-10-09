// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.CommandNotFoundException;
import io.vlingo.xoom.cli.task.XoomTurboPropertiesLoadStep;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.SubTask;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.cli.task.TaskManager;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.cli.task.Task.GLOO;

public class GlooCommandManager implements TaskManager<List<String>> {

  private static final int SUB_TASK_INDEX = 1;

  @Override
  public void run(final List<String> args) {
    validateArgs(args);
    final String command = args.get(SUB_TASK_INDEX);
    final SubTask subTask = GLOO.subTaskOf(command);
    runSteps(subTask, args);
  }

  private void runSteps(final SubTask subTask, final List<String> args) {
    final List<OptionValue> optionValues = subTask.findOptionValues(args);

    final TaskExecutionContext context =
            TaskExecutionContext.bare()
                    .withOptions(optionValues);

    final List<TaskExecutionStep> steps =
            Arrays.asList(new XoomTurboPropertiesLoadStep(), subTask.commandResolverStep());

    steps.forEach(step -> step.processTaskWith(context));
  }

  private void validateArgs(final List<String> args) {
    if(args.size() < 1) {
      throw new CommandNotFoundException();
    }
  }

}
