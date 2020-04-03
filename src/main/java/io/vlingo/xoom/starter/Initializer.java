package io.vlingo.xoom.starter;

import io.vlingo.xoom.starter.template.TemplateGenerator;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public class Initializer {

    private static final String ROOT_FOLDER = "user.dir";

    public static void main(final String[] args) throws InterruptedException {
        TemplateGenerator.start(new TemplateGenerationContext());
        System.exit(0);
    }

}
