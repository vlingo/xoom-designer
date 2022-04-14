// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.docker;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;

import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.cli.XoomTurboProperties.DOCKER_IMAGE;

public class DockerStatusTask extends Task {

  private final XoomTurboProperties xoomTurboProperties;
  private final CommandExecutionProcess commandExecutionProcess;

  public DockerStatusTask(final CommandExecutionProcess commandExecutionProcess,
                          final XoomTurboProperties xoomTurboProperties) {
    super("docker status", Option.required(CURRENT_DIRECTORY));
    this.commandExecutionProcess = commandExecutionProcess;
    this.xoomTurboProperties = xoomTurboProperties;
  }

  @Override
  public void run(final List<String> args) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        final String image = xoomTurboProperties.get(DOCKER_IMAGE);
        if (image == null) {
          throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
        }
        return String.format("docker ps --filter ancestor=%s", image);
      }
    }.execute();
  }
}
