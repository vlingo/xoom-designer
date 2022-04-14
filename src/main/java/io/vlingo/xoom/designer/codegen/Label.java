// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.parameter.ParameterLabel;

public enum Label implements ParameterLabel {

  ID("id"),
  ID_TYPE("idType"),
  BODY("body"),
  BODY_TYPE("bodyType"),
  APPLICATION_NAME("appName"),
  WEB_UI_DIALECT("webUIDialect"),
  TURBO_SETTINGS("turboSettings"),
  CLUSTER_SETTINGS("clusterSettings"),
  SCHEMATA_SETTINGS("schemataSettings"),
  PACKAGE("package"),
  AGGREGATE("aggregate"),
  STATE_FIELD("stateField"),
  FIELD_TYPE("fieldType"),
  DOMAIN_EVENT("domainEvent"),
  AGGREGATE_METHOD("aggregateMethod"),
  ALIAS("alias"),
  COLLECTION_MUTATION("collectionMutation"),
  METHOD_PARAMETER("methodParameter"),
  FACTORY_METHOD("factoryMethod"),
  COLLECTION_TYPE("collectionType"),
  REQUIRE_ENTITY_LOADING("requireEntityLoad"),
  STORAGE_TYPE("storage.type"),
  CQRS("cqrs"),
  PROJECTION_TYPE("projections"),
  PROJECTABLES("projectables"),
  PROJECTION_ACTOR("projectionActor"),
  SOURCE("source"),
  DATABASE("database"),
  REST_RESOURCES("resources"),
  APPLICATION_MAIN_CLASS("main.class"),
  DEPLOYMENT_SETTINGS("deployment"),
  BLOCKING_MESSAGING("blocking.messaging"),
  XOOM_INITIALIZER_NAME("xoom.initialization.classname"),
  COMMAND_MODEL_DATABASE("command.model.database"),
  QUERY_MODEL_DATABASE("query.model.database"),
  AUTO_DISPATCH_NAME("autoDispatchName"),
  HANDLERS_CONFIG_NAME("handlersConfigName"),
  MODEL_PROTOCOL("modelProtocol"),
  MODEL_ACTOR("modelActor"),
  MODEL_DATA("modelData"),
  QUERIES_PROTOCOL("queriesProtocol"),
  QUERIES_ACTOR("queriesActor"),
  URI_ROOT("uriRoot"),
  ROUTE_SIGNATURE("routeSignature"),
  ROUTE_PATH("routePath"),
  ROUTE_METHOD("routeMethod"),
  ROUTE_HANDLER_INVOCATION("routeHandlerInvocation"),
  READ_ONLY("readOnly"),
  USE_CUSTOM_ROUTE_HANDLER_PARAM("useCustomRouteHandlerParam"),
  ADAPTER_HANDLER_INVOCATION("adapterHandlerIndex"),
  USE_CUSTOM_ADAPTER_HANDLER_PARAM("useCustomAdapterHandlerParam"),
  USE_ADAPTER("useAdapter"),
  INTERNAL_ROUTE_HANDLER("customRoute"),
  USE_ANNOTATIONS("annotations"),
  USE_AUTO_DISPATCH("useAutoDispatch"),
  GROUP_ID("groupId"),
  ARTIFACT_ID("artifactId"),
  ARTIFACT_VERSION("artifactVersion"),
  XOOM_VERSION("xoomVersion"),
  DESIGNER_MODEL_JSON("designerModelJson"),
  TARGET_FOLDER("targetFolder"),
  LOCAL_TYPE("localType"),
  EXCHANGE("exchange"),
  SCHEMA("schema"),
  SCHEMA_GROUP("schemaGroup"),
  RECEIVER("receiver"),
  MODEL_METHOD("modelMethod"),
  ROLE("role"),
  DIALECT("dialect"),
  VALUE_OBJECT("valueObject"),
  AGGREGATE_STATE("aggregateState"),
  VALUE_OBJECT_FIELD("valueObjectField"),
  UI_TYPE("uiType");

  @SuppressWarnings("unused")
  private final String key;

  Label(final String key) {
    this.key = key;
  }
}
