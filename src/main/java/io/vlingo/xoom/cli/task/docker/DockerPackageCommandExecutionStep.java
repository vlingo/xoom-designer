// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.docker;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.designer.infrastructure.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.Terminal;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.cli.option.OptionName.TAG;

public class DockerPackageCommandExecutionStep extends CommandExecutor implements TaskExecutionStep {

  private static final String COMMAND_PATTERN = "%s && mvn clean package && docker build ./ -t %s:%s";

  public DockerPackageCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final String tag = context.optionValueOf(TAG);
    final String image = context.propertyOf(XoomTurboProperties.DOCKER_IMAGE);
    final String projectDirectoryCommand =
            Terminal.supported().resolveDirectoryChangeCommand(context.optionValueOf(CURRENT_DIRECTORY));

    if (image == null) {
      throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
    }

    return String.format(COMMAND_PATTERN, projectDirectoryCommand, image, tag);
  }

}
