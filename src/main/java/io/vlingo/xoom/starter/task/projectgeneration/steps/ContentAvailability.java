// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum ContentAvailability {

    DOCKER("Dockerfile",
            context -> {
                final DeploymentType deploymentType = context.deploymentType();
                return deploymentType.isDocker() || deploymentType.isKubernetes();
            }),

    KUBERNETES("deployment", context -> context.deploymentType().isKubernetes());

    private final String relativePath;
    private final Predicate<TaskExecutionContext> condition;

    ContentAvailability(final String relativePath,
                        final Predicate<TaskExecutionContext> condition) {
        this.relativePath = relativePath;
        this.condition = condition;
    }

    public boolean shouldBeAvailable(final TaskExecutionContext context) {
        return condition.test(context);
    }

    public String resolvePath(final TaskExecutionContext context) {
        return Paths.get(context.projectPath(), relativePath).toString();
    }

    public static List<ContentAvailability> availabilities() {
        return Arrays.asList(values());
    }
}
