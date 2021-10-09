// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskManager;
import io.vlingo.xoom.designer.Configuration;

import java.util.List;

public class UserInterfaceManager implements TaskManager<List<String>> {

  @Override
  public void run(final List<String> args) {
    final TaskExecutionContext context =
            TaskExecutionContext.withOptions(Task.DESIGNER.findOptionValues(args));

    Configuration.GUI_STEPS.stream().filter(step -> step.shouldProcess(context))
            .forEach(step -> step.processTaskWith(context));
  }

}
