package io.vlingo.xoom.starter.containerization.docker;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskExecutionStep;

public class DockerSettingsLoadStep implements TaskExecutionStep {

    private final static String DOCKER_SETTINGS_FILE = "vlingo-xoom.properties";

    @Override
    public void process(final TaskExecutionContext context) {

    }

}
