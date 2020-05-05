// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseGenerator {
    protected final Configuration configuration;
    protected final Map<String, Object> input;
    protected String template;

    public BaseGenerator() {
        this.configuration = configure();
        this.input = new HashMap<>();
    }

    public String generate() {
        return this.generate(template);
    }

    public String generate(final String templateName) {
        try {
            final Template template = configuration.getTemplate("codegen/" + templateName + ".ftl");
            final Writer consoleWriter = new StringWriter();
            template.process(input, consoleWriter);
            return consoleWriter.toString();
        } catch (final IOException | TemplateException exception) {
            throw new TemplateGenerationException(exception);
        }
    }

    public void input(final String key, final String value) {
        this.input.put(key, value);
    }

    public void inputOfPackageName(final String packageName) {
        this.input("packageName", packageName);
    }

    private Configuration configure() {
        final Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(this.getClass(), "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocale(Locale.US);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return configuration;
    }
}
