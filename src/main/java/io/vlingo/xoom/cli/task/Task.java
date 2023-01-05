// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.cli.CommandNotFoundException;
import io.vlingo.xoom.cli.UnknownCommandException;
import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Task {

  public final String command;
  public final String alternativeCommand;
  protected final List<Option> options = new ArrayList<>();

  protected Task(final String command,
                 final Option ...options) {
    this(command, null, options);
  }

  protected Task(final String command,
                 final String alternativeCommand,
                 final Option ...options) {
    this.command = command;
    this.alternativeCommand = alternativeCommand;
    this.options.addAll(Arrays.asList(options));
  }

  public abstract void run(final List<String> args);

  public static Task triggeredBy(final String command) {
    return allTasks().stream()
            .filter(task -> command != null && task.matchCommand(command.trim()))
            .findFirst().orElseThrow(() -> new UnknownCommandException(command));
  }

  public static String resolveDefaultCommand() {
    return allTasks().stream().filter(Task::isDefault).map(Task::command).findFirst()
            .orElseThrow(() -> new CommandNotFoundException());
  }

  protected String optionValueOf(final OptionName optionName, final List<String> args) {
    return OptionValue.resolveValues(options, args).stream()
            .filter(optionValue -> optionValue.hasName(optionName))
            .map(optionValue -> optionValue.value()).findFirst().get();
  }

  private boolean matchCommand(final String command) {
    return this.command.equals(command) || this.matchAlternativeCommand(command);
  }

  private boolean matchAlternativeCommand(final String command) {
    return hasAlternativeCommand() && this.alternativeCommand.equals(command);
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

  public static boolean isCommand(final String arg) {
    return !Option.isPrefixed(arg);
  }

  private static List<Task> allTasks() {
    return ComponentRegistry.withName("cliTasks");
  }

}
