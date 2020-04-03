package io.vlingo.xoom.starter.template;

import io.vlingo.xoom.starter.template.steps.*;

import java.util.Arrays;
import java.util.List;

public class TemplateGenerator {

    private static final List<TemplateGenerationStep> STEPS = Arrays.asList(
            new PropertiesLoadStep(), new ArchetypeResolverStep(), new DefaultCommandExecutorStep(),
            new WindowsCommandExecutorStep(), new GenerationLogStep(), new StatusHandlingStep()
    );

    public static void start(final TemplateGenerationContext context) {
        try {
            STEPS.forEach(step -> {
                if (step.shouldProcess()) {
                    step.process(context);
                }
            });
        } catch (final TemplateGenerationException exception) {
            exception.printStackTrace();
        }
    }

}
