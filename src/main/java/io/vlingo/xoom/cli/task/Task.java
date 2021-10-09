// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.cli.UnknownCommandException;
import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.docker.DockerCommandManager;
import io.vlingo.xoom.cli.task.gloo.GlooCommandManager;
import io.vlingo.xoom.cli.task.k8s.KubernetesCommandManager;
import io.vlingo.xoom.cli.task.version.VersionDisplayManager;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.infrastructure.userinterface.UserInterfaceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.vlingo.xoom.cli.option.OptionName.*;

@SuppressWarnings("rawtypes")
public enum Task {

  //TODO: Extract enum values to class and eliminate managers
  DOCKER("docker", new DockerCommandManager()),
  K8S("k8s", new KubernetesCommandManager()),
  GLOO("gloo", new GlooCommandManager()),
  VERSION("-version", new VersionDisplayManager()),
  DESIGNER("gui", new UserInterfaceManager(), Option.of(TARGET, GenerationTarget.FILESYSTEM.key()), Option.of(PROFILE, Profile.PRODUCTION.name()),  Option.of(PORT, "0"));

  public final String command;
  public final TaskManager manager;
  private final List<Option> options;

  Task(final String command,
       final TaskManager manager,
       final Option...options) {
    this.command = command;
    this.manager = manager;
    this.options = Arrays.asList(options);
  }

  public static <T> TaskManager<T> managerOf(final String command, final T args) {
    final Task matchedTasks =
            of(command).orElseThrow(() -> new UnknownCommandException(command));

    if(!matchedTasks.manager.support(args)) {
      throw new UnknownCommandException(args);
    }

    return matchedTasks.manager;
  }

  public static Optional<Task> of(final String command) {
    return Arrays.stream(values())
            .filter(task -> task.triggeredBy(command))
            .findFirst();
  }

  public static String resolveDefaultCommand() {
    return Task.DESIGNER.command;
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

  public SubTask subTaskOf(final String command) {
    final Predicate<SubTask> matchCondition =
            subTask -> subTask.isChildFrom(this) && subTask.triggeredBy(command);

    return Arrays.stream(SubTask.values())
            .filter(matchCondition).findFirst()
            .orElseThrow(() -> new UnknownCommandException(command));
  }

  public boolean shouldAutomaticallyExit() {
    return !equals(DESIGNER);
  }
}