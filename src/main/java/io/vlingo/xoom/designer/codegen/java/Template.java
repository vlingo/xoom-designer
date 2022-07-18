// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

public enum Template {

  ANNOTATED_BOOTSTRAP("AnnotatedBootstrap"),
  DEFAULT_BOOTSTRAP("DefaultBootstrap"),
  AGGREGATE_PROTOCOL("AggregateProtocol"),
  AGGREGATE_PROTOCOL_INSTANCE_METHOD("AggregateProtocolInstanceMethod"),
  AGGREGATE_PROTOCOL_STATIC_METHOD("AggregateProtocolStaticMethod"),
  AGGREGATE_STATE_METHOD("AggregateStateMethod"),
  OBJECT_ENTITY("ObjectEntity"),
  STATEFUL_ENTITY("StatefulEntity"),
  STATEFUL_ENTITY_METHOD("StatefulEntityMethod"),
  EVENT_SOURCE_ENTITY("EventSourcedEntity"),
  EVENT_SOURCE_ENTITY_METHOD("EventSourcedEntityMethod"),
  AGGREGATE_STATE("AggregateState"),
  DOMAIN_EVENT("DomainEvent"),
  JOURNAL_PROVIDER("JournalProvider"),
  PERSISTENCE_SETUP("PersistenceSetup"),
  STATE_STORE_PROVIDER("StateStoreProvider"),
  OBJECT_STORE_PROVIDER("ObjectStoreProvider"),
  ENTRY_ADAPTER("EntryAdapter"),
  STATE_ADAPTER("StateAdapter"),
  REST_RESOURCE("RestResource"),
  REST_UI_RESOURCE("UiResource"),
  AUTO_DISPATCH_MAPPING("AutoDispatchMapping"),
  AUTO_DISPATCH_ROUTE("AutoDispatchRoute"),
  AUTO_DISPATCH_HANDLERS_MAPPING("AutoDispatchHandlersMapping"),
  AUTO_DISPATCH_HANDLER_ENTRY("AutoDispatchHandlerEntry"),
  XOOM_INITIALIZER("XoomInitializer"),
  PROJECTION_DISPATCHER_PROVIDER("ProjectionDispatcherProvider"),
  OPERATION_BASED_PROJECTION("OperationBasedProjection"),
  EVENT_BASED_PROJECTION("EventBasedProjection"),
  STATE_DATA_OBJECT("StateDataObject"),
  VALUE_DATA_OBJECT("ValueDataObject"),
  PROJECTION_SOURCE_TYPES("ProjectionSourceTypes"),
  DATABASE_PROPERTIES("DatabaseProperties"),
  QUERIES("Queries"),
  REST_RESOURCE_CREATION_METHOD("RestResourceCreationMethod"),
  REST_RESOURCE_RETRIEVE_METHOD("RestResourceRetrieveMethod"),
  REST_RESOURCE_UPDATE_METHOD("RestResourceUpdateMethod"),
  QUERIES_ACTOR("QueriesActor"),
  DESIGNER_MODEL("DesignerModel"),
  CONSUMER_EXCHANGE_ADAPTER("ConsumerExchangeAdapter"),
  PRODUCER_EXCHANGE_ADAPTER("ProducerExchangeAdapter"),
  EXCHANGE_RECEIVER_HOLDER("ExchangeReceiverHolder"),
  CONSUMER_EXCHANGE_MAPPER("ConsumerExchangeMapper"),
  PRODUCER_EXCHANGE_MAPPER("ProducerExchangeMapper"),
  EXCHANGE_PROPERTIES("ExchangeProperties"),
  EXCHANGE_BOOTSTRAP("ExchangeBootstrap"),
  EXCHANGE_DISPATCHER("ExchangeDispatcher"),
  SCHEMATA_SPECIFICATION("SchemataSpecification"),
  SCHEMATA_PLUGIN("SchemataPlugin"),
  SCHEMATA_DNS("SchemataDNS"),
  CLUSTER_SETTINGS("ClusterSettings"),
  VALUE_OBJECT("ValueObject"),
  QUERIES_UNIT_TEST("QueriesUnitTest"),
  README("Readme"),
  EVENT_BASED_PROJECTION_UNIT_TEST("EventBasedProjectionUnitTest"),
  OPERATION_BASED_PROJECTION_UNIT_TEST("OperationBasedProjectionUnitTest"),
  OPERATION_BASED_MOCK_DISPATCHER("OperationBasedMockDispatcher"),
  EVENT_BASED_MOCK_DISPATCHER("EventBasedMockDispatcher"),
  EVENT_SOURCED_ENTITY_UNIT_TEST("EventSourcedEntityUnitTest"),
  STATEFUL_ENTITY_UNIT_TEST("StatefulEntityUnitTest"),
  REST_RESOURCE_UNIT_TEST("RestResourceUnitTest"),
  ABSTRACT_REST_RESOURCE_UNIT_TEST("RestResourceAbstractUnitTest"),
  COUNTING_READ_RESULT("CountingReadResultInterest"),
  COUNTING_PROJECTION_CTL("CountingProjectionControl"),
  TURBO_SETTINGS("TurboSettings"),
  ACTOR_SETTINGS("ActorSettings"),
  LOGBACK_SETTINGS("LogbackSettings"),
  MAVEN_SETTINGS("MavenSettings"),
  DOCKERFILE("Dockerfile"),
  KUBERNETES_MANIFEST_FILE("KubernetesManifestFile"),
  DOCKER_COMPOSE("DockerCompose"),
  DOCKER_COMPOSE_POSTGRES_SERVICE("DockerComposePostgresService"),
  DOCKER_COMPOSE_MYSQL_SERVICE("DockerComposeMySqlService"),
  DOCKER_COMPOSE_HSQLDB_SERVICE("DockerComposeHsqlDbService"),
  DOCKER_COMPOSE_YUGABYTEDB_SERVICE("DockerComposeYugaByteDbService");

  public final String filename;

  Template(final String filename) {
    this.filename = filename;
  }

}
