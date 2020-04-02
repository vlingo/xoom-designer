package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;

public interface TemplateGenerationStep {

    void process(final TemplateGenerationContext context);

    default boolean shouldProcess() {
        return true;
    }

}
