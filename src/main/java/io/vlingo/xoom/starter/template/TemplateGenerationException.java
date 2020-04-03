package io.vlingo.xoom.starter.template;

public class TemplateGenerationException extends RuntimeException {

    public TemplateGenerationException(final Exception exception) {
        super(exception);
    }

    public TemplateGenerationException(final String message) {
        super(message);
    }

}
