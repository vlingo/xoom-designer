// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.DockerCommandManager;
import io.vlingo.xoom.starter.task.gui.UserInterfaceManager;
import io.vlingo.xoom.starter.task.option.Option;
import io.vlingo.xoom.starter.task.template.TemplateGenerationManager;
import io.vlingo.xoom.starter.task.version.VersionDisplayManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum Task {

    TEMPLATE_GENERATION("gen", new TemplateGenerationManager()),
    DOCKER("docker", new DockerCommandManager()),
    GRAPHICAL_USER_INTERFACE("gui", new UserInterfaceManager()),
    VERSION("-version", new VersionDisplayManager());

    private final String command;
    private final TaskManager manager;
    private final List<Option> options;

    Task(final String command, final TaskManager manager, final Option... options) {
        this.command = command;
        this.manager = manager;
        this.options = Arrays.asList(options);
    }

    public static Task trigger(final String command) {
        return Arrays.asList(values()).stream()
                .filter(task -> task.triggeredBy(command))
                .findFirst().orElseThrow(() -> new UnknownCommandException(command));
    }

    public void run(final List<String> args) {
        this.manager.run(args);
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
