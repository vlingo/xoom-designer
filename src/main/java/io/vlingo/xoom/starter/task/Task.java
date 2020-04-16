package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.DockerCommandManager;
import io.vlingo.xoom.starter.task.template.TemplateGenerationManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum Task {

    TEMPLATE_GENERATION("gen", new TemplateGenerationManager()),
    DOCKER("docker", new DockerCommandManager());

    private final String command;
    private final TaskManager manager;

    Task(final String command, final TaskManager manager) {
        this.command = command;
        this.manager = manager;
    }

    public static Task trigger(final String command) {
        return Arrays.asList(values()).stream()
                .filter(task -> task.triggeredBy(command))
                .findFirst().orElseThrow(() -> new UnknownCommandException(command));
    }

    public void run(final List<String> args) {
        this.manager.run(args);
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
