package io.vlingo.xoomstarter.template;

public class TemplateGenerationException extends RuntimeException {

    public TemplateGenerationException(final Exception exception) {
        super(exception);
    }

    public TemplateGenerationException(final String message) {
        super(message);
    }

}
