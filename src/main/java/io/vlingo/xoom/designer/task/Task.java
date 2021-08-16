// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.designer.infrastructure.userinterface.UserInterfaceManager;
import io.vlingo.xoom.designer.task.version.VersionDisplayManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.OptionName.TARGET;

@SuppressWarnings("rawtypes")
public enum Task {

  DESIGNER_UI("gui", new UserInterfaceManager(), Option.of(TARGET, "")),
  VERSION("-version", new VersionDisplayManager());

  public final String command;
  private final TaskManager manager;
  private final List<Option> options;

  Task(final String command,
       final TaskManager manager,
       final Option...options) {
    this.command = command;
    this.manager = manager;
    this.options = Arrays.asList(options);
  }

  public static <T> TaskManager<T> of(final String command, final T args) {
    final List<Task> matchedTasks =
            Arrays.asList(values()).stream()
                    .filter(task -> task.triggeredBy(command))
                    .collect(Collectors.toList());

    if(matchedTasks.isEmpty()) {
      throw new UnknownCommandException(command);
    }

    return findManager(matchedTasks, args);
  }

  public static <T> TaskManager<T> of(final Task task, final T args) {
    return findManager(Arrays.asList(task), args);
  }

  @SuppressWarnings("unchecked")
  private static <T> TaskManager<T> findManager(final List<Task> tasks, final T args) {
    return tasks.stream().map(task -> task.manager).filter(manager -> manager.support(args))
            .findFirst().orElseThrow(() -> new UnknownCommandException(args));
  }

  public List<OptionValue> findOptionValues(final List<String> args) {
    return OptionValue.resolveValues(this.options, args);
  }

  @SuppressWarnings("unchecked")
  public <T extends TaskManager> T manager() {
    return (T) manager;
  }

  public String command() {
    return command;
  }

  private boolean triggeredBy(final String command) {
    return this.command.trim().equalsIgnoreCase(command);
  }


}