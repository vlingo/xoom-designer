// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public abstract class BaseGenerator {
  protected final Configuration configuration;
  protected final Map<String, Object> input;

  public BaseGenerator() {
    this.configuration = configure();
    this.input = new HashMap<>();
  }

  public String generate(final String templateName) throws Exception {
    final Template template = configuration.getTemplate("codegen/" + templateName + ".ftl");

    final Writer consoleWriter = new StringWriter();

    template.process(input, consoleWriter);

    return consoleWriter.toString();
  }

  public void inputOfPackageName(final String packageName) {
    input.put("packageName", packageName);
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
