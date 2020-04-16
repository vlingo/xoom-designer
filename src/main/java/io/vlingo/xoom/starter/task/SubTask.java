// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.steps.DockerPackageCommandResolverStep;
import io.vlingo.xoom.starter.task.option.Option;
import io.vlingo.xoom.starter.task.option.OptionValue;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.starter.task.option.OptionName.TAG;

public enum SubTask {

    DOCKER_STATUS(Task.DOCKER, "status", null,
            Option.required(CURRENT_DIRECTORY)),

    DOCKER_PACKAGE(Task.DOCKER, "package",
            new DockerPackageCommandResolverStep(),
            Option.required(CURRENT_DIRECTORY),
            Option.of(TAG, "latest")),

    DOCKER_PUSH(Task.DOCKER, "push", null,
            Option.required(CURRENT_DIRECTORY),
            Option.of(TAG, "latest"));

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
