// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.docker;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.CommandExecutionStep;

import static io.vlingo.xoom.designer.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.designer.task.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.designer.task.OptionName.TAG;

public class DockerPackageCommandExecutionStep extends CommandExecutionStep {

  private static final String COMMAND_PATTERN = "%s && mvn clean package && docker build ./ -t %s:%s";

  public DockerPackageCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String tag = context.optionValueOf(TAG);
        final String image = context.propertyOf(DOCKER_IMAGE);
        final String projectDirectoryCommand =
                Terminal.supported().resolveDirectoryChangeCommand(context.optionValueOf(CURRENT_DIRECTORY));

        if(image == null) {
            throw new DockerCommandException("Please set the docker.image property in xoom-turbo.properties");
        }

        return String.format(COMMAND_PATTERN, projectDirectoryCommand, image, tag);
    }

}
