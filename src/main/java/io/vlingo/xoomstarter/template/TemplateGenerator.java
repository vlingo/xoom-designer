package io.vlingo.xoomstarter.template;

import io.vlingo.xoomstarter.template.steps.*;

import java.util.Arrays;
import java.util.List;

public class TemplateGenerator {

    private static final List<TemplateGenerationStep> STEPS = Arrays.asList(
            new PropertiesLoadStep(), new ArchetypeResolverStep(), new DefaultCommandExecutorStep(),
            new WindowsCommandExecutorStep(), new GenerationLogStep(), new StatusHandlingStep()
    );

    public static void start(final TemplateGenerationContext context) {
        STEPS.forEach(step ->{
            if(step.shouldProcess()) {
                step.process(context);
            }
        });
    }

}
