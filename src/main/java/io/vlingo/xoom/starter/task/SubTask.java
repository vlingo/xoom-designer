package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.containerization.docker.DockerPackageCommandStep;

import java.util.Arrays;
import java.util.List;

public enum SubTask {

    DOCKER_STATUS(Task.DOCKER, "status", null,
            Option.required("currentDirectory")),

    DOCKER_PACKAGE(Task.DOCKER, "package", new DockerPackageCommandStep(),
            Option.required("currentDirectory"),
            Option.of("tag", "latest")),

    DOCKER_PUSH(Task.DOCKER, "push", null,
            Option.required("currentDirectory"),
            Option.of("tag", "latest"));

    private final Task parentTask;
    private final String command;
    private final TaskExecutionStep commandExecutionStep;
    private final List<Option> options;

    SubTask(final Task parentTask,
            final String command,
            final TaskExecutionStep commandExecutionStep,
            final Option ...options) {
        this.parentTask = parentTask;
        this.command = command;
        this.commandExecutionStep = commandExecutionStep;
        this.options = Arrays.asList(options);
    }

    public boolean triggeredBy(final String command) {
        return this.command.trim().equalsIgnoreCase(command);
    }

    public boolean isChildFrom(final Task task) {
        return this.parentTask.equals(task);
    }

    public TaskExecutionStep commandExecutionStep() {
        return commandExecutionStep;
    }

}
