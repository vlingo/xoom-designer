// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.reactjs;

import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.Aggregate;
import io.vlingo.xoom.designer.codegen.java.AggregateMethod;

import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.reactjs.TemplateParameter.AGGREGATE;
import static io.vlingo.xoom.designer.codegen.reactjs.TemplateParameter.METHOD;

public enum ReactJsTemplateStandard implements TemplateStandard {

  AGGREGATE_DETAIL("AggregateDetail", params -> {
    final Aggregate aggregate = params.find(AGGREGATE);
    return TemplateCustomFunctions.instance().capitalize(aggregate.aggregateName);
  }),

  AGGREGATE_LIST("AggregateList", params -> {
    final Aggregate aggregate = params.find(AGGREGATE);
    return TemplateCustomFunctions.instance().makePlural(aggregate.aggregateName);
  }),

  AGGREGATE_METHOD("AggregateMethod", params -> {
    final Aggregate aggregate = params.find(AGGREGATE);
    final AggregateMethod method = params.find(METHOD);

    final String capitalizedAggregateName =
            TemplateCustomFunctions.instance().capitalize(aggregate.aggregateName);

    final String capitalizedMethodName =
            TemplateCustomFunctions.instance().capitalize(method.name);

    return capitalizedAggregateName + capitalizedMethodName;
  }),

  APP("App", params -> "App"),
  FORM_HANDLER("FormHandler", params -> "FormHandler"),
  FORM_MODAL("FormModal", params -> "FormModal"),
  GIT_IGNORE("gitignore", params -> ".gitignore"),
  HEADER("Header", params -> "Header"),
  HOME("Home", params -> "Home"),
  HTML_INDEX("index.html", params -> "index.html"),
  INDEX("index", params -> "index"),
  LOADING_OR_FAILED("LoadingOrFailed", params -> "LoadingOrFailed"),
  PACKAGE_CONFIG("package.json", params -> "package.json"),
  SIDEBAR("Sidebar", params -> "Sidebar"),
  STYLE_SHEET_INDEX("index.css", params -> "index.css");

  private final String templateFile;
  private final Function<TemplateParameters, String> nameResolver;

  ReactJsTemplateStandard(final String templateFile,
                          final Function<TemplateParameters, String> nameResolver) {
    this.templateFile = templateFile;
    this.nameResolver = nameResolver;
  }

  @Override
  public String resolveClassname() {
    return nameResolver.apply(null);
  }

  @Override
  public String resolveClassname(final TemplateParameters parameters) {
    return nameResolver.apply(parameters);
  }

  @Override
  public String resolveFilename(final TemplateParameters parameters) {
    return nameResolver.apply(parameters);
  }

  @Override
  public String retrieveTemplateFilename(final TemplateParameters parameters) {
    return templateFile;
  }

  public static Stream<TemplateStandard> staticFilesStandards() {
    return Stream.of(FORM_HANDLER, FORM_MODAL, GIT_IGNORE, HEADER, HOME,
            HTML_INDEX, INDEX, LOADING_OR_FAILED, PACKAGE_CONFIG, STYLE_SHEET_INDEX);
  }

}
