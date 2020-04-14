package io.vlingo.xoom.starter.containerization.docker;

import io.vlingo.xoom.starter.task.*;

import java.util.Arrays;
import java.util.List;

public class DockerCommandManager implements TaskManager {

    private static final int SUB_TASK_INDEX = 1;

    @Override
    public void run(final String [] args) {
        validateArgs(args);

        final String command =
                args[SUB_TASK_INDEX];

        final SubTask subTask =
                Task.DOCKER.subTaskOf(command);

        runSteps(subTask.commandExecutionStep());
    }

    private void runSteps(final TaskExecutionStep commandExecutionStep) {
        final TaskExecutionContext context = new TaskExecutionContext();

        final List<TaskExecutionStep> steps =
                Arrays.asList(new DockerSettingsLoadStep(),
                        commandExecutionStep, new GenerationLogStep());

        steps.forEach(step -> step.process(context));
    }

    private void validateArgs(final String [] args) {
        if(args.length < 2) {
            throw new CommandNotFoundException();
        }
    }


}
