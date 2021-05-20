// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.ExchangeRole;

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

    KUBERNETES("deployment", context -> context.deploymentType().isKubernetes()),

    SCHEMATA(Paths.get("src", "main", "vlingo").toString(),
            context -> context.codeGenerationParameters().retrieveAll(Label.AGGREGATE)
                    .flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
                    .anyMatch(exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer()));

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
        return Paths.get(context.targetFolder(), relativePath).toString();
    }

    public static List<ContentAvailability> availabilities() {
        return Arrays.asList(values());
    }
}
