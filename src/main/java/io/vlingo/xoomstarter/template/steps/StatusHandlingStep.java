package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import io.vlingo.xoomstarter.template.TemplateGenerationException;

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
