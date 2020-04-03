package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public interface TemplateGenerationStep {

    void process(final TemplateGenerationContext context);

    default boolean shouldProcess() {
        return true;
    }

}
