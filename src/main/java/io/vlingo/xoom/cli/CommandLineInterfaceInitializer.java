// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.task.Task;

import java.util.Arrays;
import java.util.List;

public class CommandLineInterfaceInitializer {

  private static final int MAIN_COMMAND_INDEX = 0;
  private static final Logger logger = Logger.basicLogger();

  public static void main(final String[] args) {
    final List<String> preparedArgs = prepareArgs(args);

    final Task task =
            Task.of(preparedArgs.get(MAIN_COMMAND_INDEX))
                    .orElseThrow(() -> new UnknownCommandException(args));

    run(task, preparedArgs);
  }

  private static void run(final Task task, final List<String> args) {
    try {
      task.manager.run(args);
    } catch (final Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw exception;
    } finally {
      if(task.shouldAutomaticallyExit()) {
        System.exit(0);
      }
    }
  }

  private static List<String> prepareArgs(final String[] args) {
    if(args.length  == 0) {
      return Arrays.asList(Task.resolveDefaultCommand());
    }
    return Arrays.asList(args);
  }

}
