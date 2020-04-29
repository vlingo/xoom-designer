// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class CommandModelStateStoreProviderGenerator {
  private final Configuration configuration;
  private final Map<String, Object> input;
  private final List<ImportHolder> importsHolder;
  private final List<StateAdapterHolder> stateAdaptersHolder;

  public CommandModelStateStoreProviderGenerator() {
    this.configuration = configure();

    this.input = new HashMap<>();

    this.importsHolder = new ArrayList<>();
    this.input.put("imports", importsHolder);

    this.stateAdaptersHolder = new ArrayList<>();
    input.put("stateAdapters", stateAdaptersHolder);
  }

  public void generate() throws Exception {
    final Template template = configuration.getTemplate("codegen/CommandModelStateStoreProvider.ftl");

    final Writer consoleWriter = new OutputStreamWriter(System.out);

    template.process(input, consoleWriter);
  }

  public CommandModelStateStoreProviderGenerator inputOf(final ImportHolder importHolder) {
    importsHolder.add(importHolder);

    return this;
  }

  public CommandModelStateStoreProviderGenerator inputOf(final StateAdapterHolder stateAdapterHolder) {
    stateAdaptersHolder.add(stateAdapterHolder);

    return this;
  }

  public CommandModelStateStoreProviderGenerator inputOfPackageName(final String packageName) {
    input.put("packageName", packageName);

    return this;
  }

  public CommandModelStateStoreProviderGenerator inputOfStateStoreClassName(final String stateStoreClassName) {
    input.put("stateStoreClassName", stateStoreClassName);

    return this;
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
