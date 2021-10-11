// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.terminal;

import io.vlingo.xoom.actors.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservableCommandExecutionProcess extends CommandExecutionProcess {

  private final List<CommandExecutionObserver> observers = new ArrayList<>();

  public ObservableCommandExecutionProcess(final CommandExecutionObserver ...observers) {
    this.observers.addAll(Arrays.asList(observers));
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
    CommandOutputConsumer.of(Logger.basicLogger(), process).tail();
  }

  @Override
  protected void handleCommandExecutionStatus(final Process process) {
    try {
      final int commandExecutionStatus = process.waitFor();
      if(commandExecutionStatus == 0) {
        observers.forEach(CommandExecutionObserver::onSuccess);
      } else {
        observers.forEach(CommandExecutionObserver::onFailure);
      }
    } catch (final InterruptedException e) {
      e.printStackTrace();
      process.destroyForcibly();
      throw new CommandExecutionException(e);
    }
  }

  public interface CommandExecutionObserver {
    void onSuccess();
    void onFailure();
  }
}
