// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.template.ParameterKey;

import static io.vlingo.xoom.codegen.template.TemplateParameters.PRODUCTION_CODE_KEY;

public enum TemplateParameter implements ParameterKey {

  PACKAGE_NAME("packageName"),
  APPLICATION_NAME("appName"),
  SDK_VERSION("sdkVersion"),
  COLLECTION_MUTATIONS("collectionMutations"),
  AGGREGATES("aggregates"),
  AGGREGATE_PROTOCOL_NAME("aggregateProtocolName"),
  AGGREGATE_PROTOCOL_VARIABLE("aggregateProtocolVariable"),
  DOMAIN_EVENT_NAME("domainEventName"),
  ENTITY_NAME("entityName"),
  ENTITY_CREATION_METHOD("entityCreationMethodName"),
  STATE_DATA_OBJECT_NAME("dataName"),
  IMPORTS("imports"),
  STATE_NAME("stateName"),
  ID_NAME("idName"),
  ID_TYPE("idType"),
  USE_CQRS("useCQRS"),
  MEMBERS("members"),
  MEMBER_NAMES("memberNames"),
  MEMBERS_ASSIGNMENT("membersAssignment"),
  METHODS("methods"),
  METHOD_NAME("methodName"),
  METHOD_SCOPE("methodScope"),
  METHOD_PARAMETERS("methodParameters"),
  METHOD_INVOCATION_PARAMETERS("methodInvocationParameters"),
  CONSTRUCTOR_PARAMETERS("constructorParameters"),
  CONSTRUCTOR_INVOCATION_PARAMETERS("constructorInvocationParameters"),
  DEFAULT_SCHEMA_VERSION("defaultSchemaVersion"),
  DISPATCHER_NAME("dispatcherName"),
  ADAPTER_NAME("adapterName"),
  SOURCE_NAME("sourceName"),
  STORAGE_TYPE("storageType"),
  ENTITY_UNIT_TEST_NAME("entityUnitTestName"),
  PRODUCTION_CODE(PRODUCTION_CODE_KEY),
  UNIT_TEST("unitTest"),
  TEST_CASES("testCases"),
  AUXILIARY_ENTITY_CREATION("auxiliaryEntityCreation"),
  DATA_OBJECT_NAME("dataObjectName"),
  VALUE_OBJECT_NAME("valueObjectName"),
  DATA_VALUE_OBJECT_NAME("dataValueObjectName"),
  MODEL("model"),
  QUERY_BY_ID_METHOD_NAME("queryByIdMethodName"),
  QUERY_ALL_METHOD_NAME("queryAllMethodName"),
  REQUIRE_ADAPTERS("requireAdapters"),
  USE_PROJECTIONS("useProjections"),
  USE_ANNOTATIONS("useAnnotations"),
  PROJECTION_TYPE("projectionType"),
  ADAPTERS("adapters"),
  PERSISTENT_TYPES("persistentTypes"),
  STORE_PROVIDER_NAME("storeProviderName"),
  QUERIES("queries"),
  QUERIES_NAME("queriesName"),
  QUERIES_ACTOR_NAME("queriesActorName"),
  DATA_OBJECT_QUALIFIED_NAME("dataQualifiedName"),
  VALUE_OBJECT_TRANSLATIONS("valueObjectTranslations"),
  STATIC_FACTORY_METHODS("staticFactoryMethods"),
  STATE_FIELDS("stateFields"),
  VALUE_OBJECT_FIELDS("valueObjectFields"),
  EMPTY_OBJECT_ARGUMENTS("emptyObjectArguments"),
  PROJECTION_NAME("projectionName"),
  PROJECTION_SOURCES("sources"),
  PROJECTION_SOURCE_TYPES_NAME("projectionSourceTypesName"),
  PROJECTION_SOURCE_TYPES_QUALIFIED_NAME("projectionSourceTypesQualifiedName"),
  PROJECTION_TO_DESCRIPTION("projectToDescriptions"),
  PROJECTION_UNIT_TEST_NAME("projectionUnitTestName"),
  EVENT_SOURCED("eventSourced"),
  EVENT_HANDLERS("eventHandlers"),
  SOURCED_EVENTS("sourcedEvents"),
  QUERIES_UNIT_TEST_NAME("queriesUnitTestName"),;

  public final String key;

  TemplateParameter(String key) {
    this.key = key;
  }

  @Override
  public String value() {
    return key;
  }

}
