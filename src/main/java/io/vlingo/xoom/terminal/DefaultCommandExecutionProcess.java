// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.terminal;

import io.vlingo.xoom.actors.Logger;

import java.io.IOException;

public class DefaultCommandExecutionProcess extends CommandExecutionProcess {

  private static final String SUCCESS_MESSAGE = "Done!";
  private static final String FAILURE_MESSAGE = "Failed.";

  private final Logger logger;

  public DefaultCommandExecutionProcess(final Logger logger) {
    this.logger = logger;
  }

  @Override
  protected Process execute(final String[] commandSequence) {
    try {
      return Runtime.getRuntime().exec(commandSequence);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new CommandExecutionException(e);
    }
  }

  @Override
  protected void log(final Process process) {
    CommandOutputConsumer.of(logger, process).tail();
  }

  @Override
  protected void handleCommandExecutionStatus(final Process process) {
    try {
      final int commandExecutionStatus = process.waitFor();
      if(commandExecutionStatus == 0) {
        System.out.println(SUCCESS_MESSAGE);
      } else {
        throw new CommandExecutionException(FAILURE_MESSAGE);
      }
    } catch (final InterruptedException e) {
      e.printStackTrace();
      throw new CommandExecutionException(e);
    }
  }

}
