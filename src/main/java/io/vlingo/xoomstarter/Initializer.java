package io.vlingo.xoomstarter;

import io.vlingo.xoomstarter.template.TemplateGenerator;

public class Initializer {

    private static final String ROOT_FOLDER = "user.dir";

    public static void main(final String[] args) {
        TemplateGenerator.start();
    }

}
