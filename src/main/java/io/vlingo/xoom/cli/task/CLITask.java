// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CLITask {

  public final String command;
  public final String alternativeCommand;
  private final List<Option> options = new ArrayList<>();

  protected CLITask(final String command,
                    final List<Option> options) {
    this(command, null, options);
  }

  protected CLITask(final String command,
                    final String alternativeCommand,
                    final List<Option> options) {
    this.command = command;
    this.alternativeCommand = alternativeCommand;
    this.options.addAll(options);
  }

  public abstract void run();

  public static Optional<CLITask> triggeredBy(final String command) {
    return filterByCommand(command, ComponentRegistry.withName("cliTasks"));
  }

  protected static Optional<CLITask> filterByCommand(final String command,
                                                     final List<CLITask> tasks) {
    return tasks.stream()
            .filter(task -> command != null && task.matchCommand(command.trim()))
            .findFirst();
  }

  private boolean matchCommand(final String command) {
    return this.command.equalsIgnoreCase(command) || this.matchAlternativeCommand(command);
  }

  private boolean matchAlternativeCommand(final String command) {
    return hasAlternativeCommand() && this.alternativeCommand.equalsIgnoreCase(command);
  }

  private boolean hasAlternativeCommand() {
    return this.alternativeCommand != null;
  }

  public boolean shouldAutomaticallyExit() {
    return true;
  }

  public boolean isDefault() {
    return false;
  }

  public String command() {
    return command;
  }

}
