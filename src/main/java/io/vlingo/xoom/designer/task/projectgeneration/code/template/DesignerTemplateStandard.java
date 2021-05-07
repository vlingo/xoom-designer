// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.DataObjectDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.ExchangeRole;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.Model;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.http.Method;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Template.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType.STATE_STORE;

public enum DesignerTemplateStandard implements TemplateStandard {

  AGGREGATE_PROTOCOL(parameters -> Template.AGGREGATE_PROTOCOL.filename),

  AGGREGATE_PROTOCOL_METHOD(parameters -> parameters.<MethodScope>find(METHOD_SCOPE).isStatic() ?
          AGGREGATE_PROTOCOL_STATIC_METHOD.filename : AGGREGATE_PROTOCOL_INSTANCE_METHOD.filename),

  AGGREGATE(parameters -> AGGREGATE_TEMPLATES.get(parameters.find(STORAGE_TYPE)),
          (name, parameters) -> name + "Entity"),

  AGGREGATE_METHOD(parameters -> AGGREGATE_METHOD_TEMPLATES.get(parameters.find(STORAGE_TYPE))),

  AGGREGATE_STATE(parameters -> Template.AGGREGATE_STATE.filename,
          (name, parameters) -> name + "State"),

  AGGREGATE_STATE_METHOD(parameters -> Template.AGGREGATE_STATE_METHOD.filename),

  VALUE_OBJECT(parameters -> Template.VALUE_OBJECT.filename),

  PROJECT_SETTINGS(parameters -> Template.PROJECT_SETTINGS.filename,
          (name, parameters) -> parameters.find(APPLICATION_NAME) + "-generation-settings.json"),

  QUERIES(parameters -> Template.QUERIES.filename,
          (name, parameters) -> name + "Queries"),

  QUERIES_ACTOR(parameters -> Template.QUERIES_ACTOR.filename,
          (name, parameters) -> name + "QueriesActor"),

  DATA_OBJECT(parameters -> parameters.has(STATE_DATA_OBJECT_NAME) ?
  Template.STATE_DATA_OBJECT.filename : VALUE_DATA_OBJECT.filename,
          (name, parameters) -> name + DataObjectDetail.DATA_OBJECT_NAME_SUFFIX),

  REST_RESOURCE(parameters -> Template.REST_RESOURCE.filename,
          (name, parameters) -> name + "Resource"),

  REST_UI_RESOURCE(parameters -> Template.REST_UI_RESOURCE.filename,
          (name, parameters) -> name + "Resource"),

  ROUTE_METHOD(parameters -> {
    final String httpMethod =
            parameters.find(TemplateParameter.ROUTE_METHOD);

    if (Method.from(httpMethod).isGET()) {
      return Template.REST_RESOURCE_RETRIEVE_METHOD.filename;
    }

    if (parameters.find(REQUIRE_ENTITY_LOADING, false)) {
      return Template.REST_RESOURCE_UPDATE_METHOD.filename;
    }

    return Template.REST_RESOURCE_CREATION_METHOD.filename;
  }, (name, parameters) -> name),

  AUTO_DISPATCH_RESOURCE_HANDLER(parameters -> Template.REST_RESOURCE.filename,
          (name, parameters) -> name + "Handler"),

  AUTO_DISPATCH_RESOURCE_UI_HANDLER(parameters -> Template.REST_UI_RESOURCE.filename,
          (name, parameters) -> name + "Handler"),

  AUTO_DISPATCH_MAPPING(parameters -> Template.AUTO_DISPATCH_MAPPING.filename,
          (name, parameters) -> name + "Resource"),

  AUTO_DISPATCH_HANDLER_ENTRY(parameters -> Template.AUTO_DISPATCH_HANDLER_ENTRY.filename),

  AUTO_DISPATCH_HANDLERS_MAPPING(parameters -> Template.AUTO_DISPATCH_HANDLERS_MAPPING.filename,
          (name, parameters) -> name + "ResourceHandlers"),

  AUTO_DISPATCH_ROUTE(parameters -> Template.AUTO_DISPATCH_ROUTE.filename),

  ADAPTER(parameters -> ADAPTER_TEMPLATES.get(parameters.find(STORAGE_TYPE)),
          (name, parameters) -> name + "Adapter"),

  PROJECTION(parameters -> CodeGenerationSetup.PROJECTION_TEMPLATES.get(parameters.find(PROJECTION_TYPE)),
          (name, parameters) -> name + "ProjectionActor"),

  PROJECTION_DISPATCHER_PROVIDER(parameters -> Template.PROJECTION_DISPATCHER_PROVIDER.filename,
          (name, parameters) -> "ProjectionDispatcherProvider"),

  PROJECTION_SOURCE_TYPES(parameters -> Template.PROJECTION_SOURCE_TYPES.filename,
          (name, parameters) -> {
    final ProjectionType projectionType = parameters.find(PROJECTION_TYPE);
    return projectionType.isEventBased() ? "Events" : "Operations";
  }),

  EXCHANGE_BOOTSTRAP(parameters -> Template.EXCHANGE_BOOTSTRAP.filename,
          (name, parameters) -> "ExchangeBootstrap"),

  EXCHANGE_MAPPER(parameters -> parameters.<ExchangeRole>find(EXCHANGE_ROLE).isConsumer() ?
  CONSUMER_EXCHANGE_MAPPER.filename : PRODUCER_EXCHANGE_MAPPER.filename,
          (name, parameters) -> parameters.<ExchangeRole>find(EXCHANGE_ROLE).isConsumer() ?
          parameters.find(LOCAL_TYPE_NAME) + "Mapper" : "DomainEventMapper"),

  EXCHANGE_ADAPTER(parameters -> parameters.<ExchangeRole>find(EXCHANGE_ROLE).isConsumer() ?
  CONSUMER_EXCHANGE_ADAPTER.filename : PRODUCER_EXCHANGE_ADAPTER.filename,
          (name, parameters) -> parameters.<String>find(AGGREGATE_PROTOCOL_NAME) +
  parameters.<ExchangeRole>find(EXCHANGE_ROLE).formatName() + "Adapter"),

  EXCHANGE_RECEIVER_HOLDER(parameters -> Template.EXCHANGE_RECEIVER_HOLDER.filename,
          (name, parameters) -> parameters.<String>find(AGGREGATE_PROTOCOL_NAME) +
          "ExchangeReceivers"),

  EXCHANGE_PROPERTIES(templateParameters -> Template.EXCHANGE_PROPERTIES.filename,
          (name, parameters) -> "xoom-turbo.properties"),

  XOOM_INITIALIZER(templateParameters -> Template.XOOM_INITIALIZER.filename,
          (name, parameters) -> "XoomInitializer"),

  BOOTSTRAP(parameters -> parameters.find(USE_ANNOTATIONS, false) ?
  ANNOTATED_BOOTSTRAP.filename : DEFAULT_BOOTSTRAP.filename,
          (name, parameters) -> "Bootstrap"),

  DATABASE_PROPERTIES(templateParameters -> Template.DATABASE_PROPERTIES.filename,
          (name, parameters) -> "xoom-turbo.properties"),

  DOMAIN_EVENT(parameters -> Template.DOMAIN_EVENT.filename),

  PERSISTENCE_SETUP(parameters -> Template.PERSISTENCE_SETUP.filename,
          (name, parameters) -> "PersistenceSetup"),

  SCHEMATA_SPECIFICATION(parameters -> Template.SCHEMATA_SPECIFICATION.filename,
          (name, parameters) -> parameters.find(SCHEMATA_SPECIFICATION_NAME) + ".vss"),

  SCHEMATA_PLUGIN(parameters -> Template.SCHEMATA_PLUGIN.filename,
          (name, parameters) -> "pom.xml"),

  STORE_PROVIDER(parameters -> storeProviderTemplatesFrom(parameters.find(MODEL)).get(parameters.find(STORAGE_TYPE)),
          (name, parameters) -> {
    final StorageType storageType = parameters.find(STORAGE_TYPE);
    final Model model = parameters.find(MODEL);
    if (model.isQueryModel()) {
      return STATE_STORE.resolveProviderNameFrom(model);
    }
    return storageType.resolveProviderNameFrom(model);
  }),

  EXCHANGE_DISPATCHER(parameters -> Template.EXCHANGE_DISPATCHER.filename,
          (name, parameters) -> "ExchangeDispatcher"),

  QUERIES_UNIT_TEST(parameters -> Template.QUERIES_UNIT_TEST.filename,
          (name, parameters) -> name + "Test"),

  ENTITY_UNIT_TEST(parameters -> {
    final StorageType storageType = parameters.find(STORAGE_TYPE);
    if (storageType.isSourced()) {
      return Template.EVENT_SOURCED_ENTITY_UNIT_TEST.filename;
    }
    return Template.STATEFUL_ENTITY_UNIT_TEST.filename;
  }, (name, parameters) -> name + "Test"),

  MOCK_DISPATCHER(parameters -> {
    final ProjectionType projectionType = parameters.find(PROJECTION_TYPE);
    if (projectionType.isOperationBased()) {
      return Template.OPERATION_BASED_MOCK_DISPATCHER.filename;
    }
    return Template.EVENT_BASED_MOCK_DISPATCHER.filename;
  }, (name, parameters) -> "MockDispatcher");

  private final Function<TemplateParameters, String> templateFileRetriever;
  private final BiFunction<String, TemplateParameters, String> nameResolver;

  DesignerTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever) {
    this(templateFileRetriever, (name, parameters) -> name);
  }

  DesignerTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever,
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
