// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.task.CLITask;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLineInterfaceInitializer {

  private static final int MAIN_COMMAND_INDEX = 0;
  private static final Logger logger = Logger.basicLogger();

  public static void main(final String[] args) {
    ComponentRegistry.register(Logger.class, logger);

    final List<String> preparedArgs = prepareArgs(args);

    final CLITask task =
            CLITask.triggeredBy(preparedArgs.get(MAIN_COMMAND_INDEX))
                    .orElseThrow(() -> new UnknownCommandException(args));

    run(task, preparedArgs);
  }

  private static void run(final CLITask task, final List<String> args) {
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

  private static List<String> prepareArgs(final String[] args) {
    if(args.length  == 0) {
      return Collections.singletonList(CLITask.resolveDefaultCommand());
    }
    return Arrays.asList(args);
  }

}
