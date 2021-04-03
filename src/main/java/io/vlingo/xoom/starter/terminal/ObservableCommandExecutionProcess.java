// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservableCommandExecutionProcess extends CommandExecutionProcess {

  private final List<CommandsExecutionObserver> observers = new ArrayList<>();

  public ObservableCommandExecutionProcess(final CommandsExecutionObserver ...observers) {
    this.observers.addAll(Arrays.asList(observers));
  }

  @Override
  protected Process execute(final String[] commands) {
    observers.forEach(observer -> observer.onExecution(commands));
    return null;
  }

  @Override
  protected void log(final Process process) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void handleCommandExecutionStatus(final int commandExecutionStatus) {
    throw new UnsupportedOperationException();
  }

  @FunctionalInterface
  public interface CommandsExecutionObserver {
    void onExecution(final String[] commands);
  }
}
