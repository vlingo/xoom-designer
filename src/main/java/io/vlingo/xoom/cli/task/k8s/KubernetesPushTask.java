// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.k8s;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.Terminal;

import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;

public class KubernetesPushTask extends Task {

  private final CommandExecutionProcess commandExecutionProcess;

  public KubernetesPushTask(final CommandExecutionProcess commandExecutionProcess) {
    super("k8s push", Option.required(CURRENT_DIRECTORY));
    this.commandExecutionProcess = commandExecutionProcess;
  }

  @Override
  public void run(final List<String> args) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        final String currentDirectory = optionValueOf(OptionName.CURRENT_DIRECTORY, args);

        final String projectDirectoryCommand =
                Terminal.supported().resolveDirectoryChangeCommand(currentDirectory);

        return String.format("%s && kubectl apply -f %s", projectDirectoryCommand, Paths.get("deployment", "k8s"));
      }
    }.execute();
  }

}
