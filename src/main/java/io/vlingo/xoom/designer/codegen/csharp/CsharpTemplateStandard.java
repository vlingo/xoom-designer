// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.formatting.DataObjectDetail;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.designer.codegen.csharp.Template.*;
import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.APPLICATION_NAME;
import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.METHOD_SCOPE;
import static io.vlingo.xoom.designer.codegen.java.Template.VALUE_DATA_OBJECT;
import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.STATE_DATA_OBJECT_NAME;

public enum CsharpTemplateStandard implements TemplateStandard {

  SOLUTION_SETTINGS(parameters -> Template.SOLUTION_SETTINGS.filename,
      (name, parameters) -> parameters.find(APPLICATION_NAME) + ".sln"),
  PROJECT_SETTINGS(parameters -> Template.PROJECT_SETTINGS.filename,
      (name, parameters) -> parameters.find(APPLICATION_NAME) + ".csproj"),
  README(parameters -> Template.README.filename, (name, parameters) -> "README.md"),
  UNIT_TEST_PROJECT_SETTINGS(parameters -> Template.UNIT_TEST_PROJECT_SETTINGS.filename,
      (name, parameters) -> parameters.find(APPLICATION_NAME) + ".Tests.csproj"),
  ACTOR_SETTINGS(parameters -> Template.ACTOR_SETTINGS.filename,
      (name, parameters) -> "vlingo-actors.json"),
  AGGREGATE_PROTOCOL(parameters -> Template.AGGREGATE_PROTOCOL.filename, (name, parameters) -> "I" + name),
  AGGREGATE_PROTOCOL_METHOD(parameters -> parameters.<MethodScope>find(METHOD_SCOPE).isStatic() ?
      AGGREGATE_PROTOCOL_STATIC_METHOD.filename : AGGREGATE_PROTOCOL_INSTANCE_METHOD.filename),
  AGGREGATE(parameters -> Template.STATEFUL_ENTITY.filename, (name, parameters) -> name + "Entity"),
  AGGREGATE_STATE(parameters -> Template.AGGREGATE_STATE.filename, (name, parameters) -> name + "State"),
  AGGREGATE_METHOD(parameters -> Template.STATEFUL_ENTITY_METHOD.filename),
  AGGREGATE_STATE_METHOD(parameters -> Template.AGGREGATE_STATE_METHOD.filename),
  DOMAIN_EVENT(parameters -> Template.DOMAIN_EVENT.filename),
  ENTITY_UNIT_TEST(parameters -> Template.STATEFUL_ENTITY_UNIT_TEST.filename, (name, parameters) -> name + "Test"),
  MOCK_DISPATCHER(parameters -> Template.EVENT_BASED_MOCK_DISPATCHER.filename, (name, parameters) -> "MockDispatcher"),
  ADAPTER(parameters -> Template.STATE_ADAPTER.filename, (name, parameters) -> name + "Adapter"),
  BOOTSTRAP(parameters -> DEFAULT_BOOTSTRAP.filename,
      (name, parameters) -> "Bootstrap"),
  VALUE_OBJECT(parameters -> Template.VALUE_OBJECT.filename),
  DATA_OBJECT(parameters -> parameters.has(STATE_DATA_OBJECT_NAME) ?
      Template.STATE_DATA_OBJECT.filename : VALUE_DATA_OBJECT.filename,
      (name, parameters) -> name + DataObjectDetail.DATA_OBJECT_NAME_SUFFIX);

  private final Function<TemplateParameters, String> templateFileRetriever;
  private final BiFunction<String, TemplateParameters, String> nameResolver;

  CsharpTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever) {
    this(templateFileRetriever, (name, parameters) -> name);
  }

  CsharpTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever,
                         final BiFunction<String, TemplateParameters, String> nameResolver) {
    this.templateFileRetriever = templateFileRetriever;
    this.nameResolver = nameResolver;
  }

  public String retrieveTemplateFilename(final TemplateParameters parameters) {
    return templateFileRetriever.apply(parameters);
  }

  public String resolveClassname() {
    return resolveClassname("");
  }

  public String resolveClassname(final String name) {
    return resolveClassname(name, null);
  }

  public String resolveClassname(final TemplateParameters parameters) {
    return resolveClassname(null, parameters);
  }

  public String resolveClassname(final String name, final TemplateParameters parameters) {
    return this.nameResolver.apply(name, parameters);
  }

  public String resolveFilename(final TemplateParameters parameters) {
    return resolveFilename(null, parameters);
  }

  public String resolveFilename(final String name, final TemplateParameters parameters) {
    return this.nameResolver.apply(name, parameters);
  }

}
