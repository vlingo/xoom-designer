package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.IOException;
import java.nio.file.Files;

public class OutputResourceCreationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.outputResources().forEach(resource -> {
            try {
                resource.file.getParentFile().mkdirs();
                resource.file.createNewFile();
                Files.write(resource.file.toPath(), resource.content.getBytes());
            } catch (IOException e) {
                throw new TemplateGenerationException(e);
            }
        });
    }

}
