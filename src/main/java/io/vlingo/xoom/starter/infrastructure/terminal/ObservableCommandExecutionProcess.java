// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.infrastructure.terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservableCommandExecutionProcess extends CommandExecutionProcess {

  private Process process;
  private final List<CommandExecutionObserver> observers = new ArrayList<>();

  public ObservableCommandExecutionProcess(final CommandExecutionObserver...observers) {
    this.observers.addAll(Arrays.asList(observers));
  }

  @Override
  protected void execute(final String[] commandSequence) {
    try {
      this.process = Runtime.getRuntime().exec(commandSequence);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new CommandExecutionException(e);
    }
  }

  @Override
  protected void log() {}

  @Override
  protected void handleCommandExecutionStatus() {
    try {
      final int commandExecutionStatus = process.waitFor();
      if(commandExecutionStatus == 0) {
        observers.forEach(CommandExecutionObserver::onSuccess);
      } else {
        observers.forEach(CommandExecutionObserver::onFailure);
      }
    } catch (final InterruptedException e) {
      e.printStackTrace();
      throw new CommandExecutionException(e);
    }
  }

  public interface CommandExecutionObserver {
    void onSuccess();
    void onFailure();
  }
}
