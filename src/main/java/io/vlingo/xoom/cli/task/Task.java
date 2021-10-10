// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.cli.UnknownCommandException;
import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.task.gloo.GlooCommandManager;
import io.vlingo.xoom.cli.task.k8s.KubernetesCommandManager;
import io.vlingo.xoom.cli.task.version.VersionDisplayManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("rawtypes")
public enum Task {

  //TODO: Extract enum values to class and eliminate managers
  K8S("k8s", new KubernetesCommandManager()),
  GLOO("gloo", new GlooCommandManager()),
  VERSION("-version", new VersionDisplayManager());

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

  public SubTask subTaskOf(final String command) {
    final Predicate<SubTask> matchCondition =
            subTask -> subTask.isChildFrom(this) && subTask.triggeredBy(command);

    return Arrays.stream(SubTask.values())
            .filter(matchCondition).findFirst()
            .orElseThrow(() -> new UnknownCommandException(command));
  }

  public boolean shouldAutomaticallyExit() {
    return false;
  }
}