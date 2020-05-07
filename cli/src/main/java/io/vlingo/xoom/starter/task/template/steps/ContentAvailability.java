package io.vlingo.xoom.starter.task.template.steps;

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
