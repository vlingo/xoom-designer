// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.DockerCommandManager;
import io.vlingo.xoom.starter.task.gloo.GlooCommandManager;
import io.vlingo.xoom.starter.task.k8s.KubernetesCommandManager;
import io.vlingo.xoom.starter.task.projectgeneration.CommandLineBasedProjectGenerationManager;
import io.vlingo.xoom.starter.task.projectgeneration.WebBasedProjectGenerationManager;
import io.vlingo.xoom.starter.task.projectgeneration.gui.UserInterfaceManager;
import io.vlingo.xoom.starter.task.version.VersionDisplayManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Task {

    WEB_BASED_PROJECT_GENERATION("gen", new WebBasedProjectGenerationManager()),
    CLI_BASED_PROJECT_GENERATION("gen", new CommandLineBasedProjectGenerationManager()),
    DOCKER("docker", new DockerCommandManager()),
    K8S("k8s", new KubernetesCommandManager()),
    GLOO("gloo", new GlooCommandManager()),
    GRAPHICAL_USER_INTERFACE("gui", new UserInterfaceManager()),
    VERSION("-version", new VersionDisplayManager());

    public final String command;
    private final TaskManager manager;

    Task(final String command,
         final TaskManager manager) {
        this.command = command;
        this.manager = manager;
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

    private static <T> TaskManager<T> findManager(final List<Task> tasks, final T args) {
        return tasks.stream().map(task -> task.manager).filter(manager -> manager.support(args))
                .findFirst().orElseThrow(() -> new UnknownCommandException(args));
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

        return Arrays.asList(SubTask.values())
                .stream().filter(matchCondition).findFirst()
                .orElseThrow(() -> new UnknownCommandException(command));
    }

}