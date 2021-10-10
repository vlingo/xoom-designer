// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.docker;

import io.vlingo.xoom.cli.CommandNotFoundException;
import io.vlingo.xoom.cli.UnknownCommandException;
import io.vlingo.xoom.cli.task.CLITask;
import io.vlingo.xoom.terminal.CommandExecutionProcess;

import java.util.Arrays;
import java.util.List;

public class DockerTask extends CLITask {

  private final List<CLITask> subTasks;

  private static final int SUB_TASK_INDEX = 1;

  public DockerTask(final CommandExecutionProcess commandExecutionProcess) {
    super("docker");
    this.subTasks =
            Arrays.asList(new DockerPackageTask(commandExecutionProcess),
                    new DockerPushTask(commandExecutionProcess),
                    new DockerStatusTask(commandExecutionProcess));
  }

  @Override
  public void run(final List<String> args) {
    validateArgs(args);

    final String command =
            args.get(SUB_TASK_INDEX);

    filterByCommand(command, subTasks)
            .orElseThrow(() -> new UnknownCommandException(command))
            .run(args);
  }

  private void validateArgs(final List<String> args) {
    if (args.size() < 2) {
      throw new CommandNotFoundException();
    }
  }

}
