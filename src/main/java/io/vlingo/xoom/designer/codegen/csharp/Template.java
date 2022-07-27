// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

public enum Template {
  SOLUTION_SETTINGS("Solution"),
  PROJECT_SETTINGS("Project"),
  README("Readme"),
  UNIT_TEST_PROJECT_SETTINGS("UnitTestProject"),
  ACTOR_SETTINGS("ActorSettings"),
  AGGREGATE_PROTOCOL("AggregateProtocol"),
  AGGREGATE_PROTOCOL_INSTANCE_METHOD("AggregateProtocolInstanceMethod"),
  AGGREGATE_PROTOCOL_STATIC_METHOD("AggregateProtocolStaticMethod"),
  AGGREGATE_STATE("AggregateState"),
  AGGREGATE_STATE_METHOD("AggregateStateMethod"),
  STATEFUL_ENTITY("StatefulEntity"),
  STATEFUL_ENTITY_METHOD("StatefulEntityMethod"),
  DOMAIN_EVENT("DomainEvent"),
  EVENT_SOURCED_ENTITY_UNIT_TEST("EventSourcedEntityUnitTest"),
  STATEFUL_ENTITY_UNIT_TEST("StatefulEntityUnitTest"),
  OPERATION_BASED_MOCK_DISPATCHER("OperationBasedMockDispatcher"),
  EVENT_BASED_MOCK_DISPATCHER("EventBasedMockDispatcher"),
  STATE_ADAPTER("StateAdapter"),
  DEFAULT_BOOTSTRAP("DefaultBootstrap"),
  VALUE_OBJECT("ValueObject"),
  STATE_DATA_OBJECT("StateDataObject");

  public final String filename;

  Template(final String filename) {
    this.filename = filename;
  }

}
