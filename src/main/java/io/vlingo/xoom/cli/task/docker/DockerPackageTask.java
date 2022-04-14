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
import io.vlingo.xoom.terminal.Terminal;

import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.cli.option.OptionName.TAG;
import static io.vlingo.xoom.cli.XoomTurboProperties.DOCKER_IMAGE;

public class DockerPackageTask extends Task {

  private final XoomTurboProperties xoomTurboProperties;
  private final CommandExecutionProcess commandExecutionProcess;

  public DockerPackageTask(final CommandExecutionProcess commandExecutionProcess,
                           final XoomTurboProperties xoomTurboProperties) {
    super("docker package", Option.required(CURRENT_DIRECTORY), Option.of(TAG, "latest"));
    this.commandExecutionProcess = commandExecutionProcess;
    this.xoomTurboProperties = xoomTurboProperties;
  }

  @Override
  public void run(final List<String> args) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        final String tag = optionValueOf(TAG, args);
        final String currentDirectory = optionValueOf(CURRENT_DIRECTORY, args);
        final String dockerImage = xoomTurboProperties.get(DOCKER_IMAGE);
        final String projectDirectoryCommand =
                Terminal.supported().resolveDirectoryChangeCommand(currentDirectory);

        if (dockerImage == null) {
          throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
        }

        return String.format("%s && mvn clean package && docker build ./ -t %s:%s", projectDirectoryCommand, dockerImage, tag);
      }
    }.execute();
  }
}
