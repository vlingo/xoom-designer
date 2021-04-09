// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.task.docker.steps.DockerPackageCommandExecutionStep;
import io.vlingo.xoom.designer.task.docker.steps.DockerPushCommandExecutionStep;
import io.vlingo.xoom.designer.task.docker.steps.DockerStatusCommandExecutionStep;
import io.vlingo.xoom.designer.task.gloo.steps.GlooInitCommandExecutionStep;
import io.vlingo.xoom.designer.task.gloo.steps.GlooRouteCommandExecutionStep;
import io.vlingo.xoom.designer.task.gloo.steps.GlooSuspendCommandExecutionStep;
import io.vlingo.xoom.designer.task.k8s.steps.KubernetesPushCommandExecutionStep;
import io.vlingo.xoom.designer.task.option.Option;
import io.vlingo.xoom.designer.task.option.OptionValue;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.designer.task.option.OptionName.TAG;
import static io.vlingo.xoom.designer.ComponentRegistry.withType;
import static io.vlingo.xoom.designer.task.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.designer.task.option.OptionName.TAG;

public enum SubTask {

    DOCKER_STATUS(Task.DOCKER, "status",
            new DockerStatusCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY)),

    DOCKER_PACKAGE(Task.DOCKER, "package",
            new DockerPackageCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY),
            Option.of(TAG, "latest")),

    DOCKER_PUSH(Task.DOCKER, "push",
            new DockerPushCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY),
            Option.of(TAG, "latest")),

    K8S_PUSH(Task.K8S, "push",
            new KubernetesPushCommandExecutionStep(withType(CommandExecutionProcess.class))),

    GLOO_INIT(Task.GLOO, "init",
            new GlooInitCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY)),

    GLOO_ROUTE(Task.GLOO, "route",
            new GlooRouteCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY)),

    GLOO_SUSPEND(Task.GLOO, "suspend",
            new GlooSuspendCommandExecutionStep(withType(CommandExecutionProcess.class)),
            Option.required(CURRENT_DIRECTORY));

    private final Task parentTask;
    private final String command;
    private final TaskExecutionStep commandResolverStep;
    private final List<Option> options;

    SubTask(final Task parentTask,
            final String command,
            final TaskExecutionStep commandResolverStep,
            final Option ...options) {
        this.parentTask = parentTask;
        this.command = command;
        this.commandResolverStep = commandResolverStep;
        this.options = Arrays.asList(options);
    }

    public List<OptionValue> findOptionValues(final List<String> args) {
        return this.options.stream().map(option -> {
            final String value = option.findValue(args);
            return OptionValue.with(option.name(), value);
        }).collect(Collectors.toList());
    }

    public TaskExecutionStep commandResolverStep() {
        return commandResolverStep;
    }

    protected boolean triggeredBy(final String command) {
        return this.command.trim().equalsIgnoreCase(command);
    }

    protected boolean isChildFrom(final Task task) {
        return this.parentTask.equals(task);
    }

}
