package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationException;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public class StatusHandlingStep implements TemplateGenerationStep {

    @Override
    public void process(final TemplateGenerationContext context) {
        try {
            final Integer status = context.process().waitFor();
            StatusHandler.forStatus(status).handle(context);
        } catch (InterruptedException e) {
            throw new TemplateGenerationException(e);
        }
    }

}
