// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.XoomTurboProperties.ProjectPath;
import io.vlingo.xoom.terminal.DefaultCommandExecutionProcess;

import java.util.Arrays;
import java.util.List;

public class CommandLineInterfaceInitializer {

  private static final Logger logger = Logger.basicLogger();

  public static void main(final String[] args) {
    ComponentsRegistration.registerWith(logger, new DefaultCommandExecutionProcess(logger), loadProperties(args));

    final Task task =
            Task.triggeredBy(resolveCommand(args))
                    .orElseThrow(() -> new UnknownCommandException(args));

    runTask(task, Arrays.asList(args));
  }

  private static void runTask(final Task task,
                              final List<String> args) {
    try {
      task.run(args);
    } catch (final Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw exception;
    } finally {
      if(task.shouldAutomaticallyExit()) {
        System.exit(0);
      }
    }
  }

  private static String resolveCommand(final String[] args) {
    if(args.length > 0) {
      if(args.length == 1 || !Task.isCommand(args[1])) {
        return args[0];
      }
      return args[0] + " " + args[1];
    }
    return Task.resolveDefaultCommand();
  }

  private static XoomTurboProperties loadProperties(final String[] args) {
    final Option projectDirectory =
            Option.of(OptionName.CURRENT_DIRECTORY, System.getProperty("user.dir"));

    final OptionValue path =
            OptionValue.resolveValue(projectDirectory, Arrays.asList(args));

    return XoomTurboProperties.load(ProjectPath.from(path.value()));
  }

}
