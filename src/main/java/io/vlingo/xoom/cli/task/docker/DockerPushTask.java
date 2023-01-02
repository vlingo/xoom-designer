// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.docker;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.Terminal;

import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.cli.option.OptionName.TAG;

public class DockerPushTask extends Task {

  private static final String LOCAL = "LOCAL";
  private static final String REMOTE = "REMOTE";

  private final XoomTurboProperties xoomTurboProperties;
  private final CommandExecutionProcess commandExecutionProcess;

  public DockerPushTask(final CommandExecutionProcess commandExecutionProcess,
                        final XoomTurboProperties xoomTurboProperties) {
    super("docker push", Option.required(CURRENT_DIRECTORY), Option.of(TAG, "latest"));
    this.commandExecutionProcess = commandExecutionProcess;
    this.xoomTurboProperties = xoomTurboProperties;
  }

  @Override
  public void run(final List<String> args) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        final String image = xoomTurboProperties.get(XoomTurboProperties.DOCKER_IMAGE);
        final String repo = xoomTurboProperties.get(XoomTurboProperties.DOCKER_REPOSITORY);
        final String currentDirectory = optionValueOf(OptionName.CURRENT_DIRECTORY, args);

        final String projectDirectoryCommand =
                Terminal.supported().resolveDirectoryChangeCommand(currentDirectory);

        if (image == null || repo == null) {
          throw new DockerCommandException("Please set Docker properties in xoom-turbo.properties");
        }

        return String.format("%s && docker tag %s:%s %s:%s && docker push %s:%s",
                projectDirectoryCommand, image, getTag(LOCAL, args),
                repo, getTag(REMOTE, args), repo, getTag(REMOTE, args));
      }
    }.execute();
  }

  private String getTag(final String type, final List<String> args) {
    final String tag = optionValueOf(TAG, args);
    if (!tag.contains(":")) {
      return tag;
    }
    final String[] tags = tag.split(":");
    return type.equals(LOCAL) ? tags[0] : tags[1];
  }

}
