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
  STATEFUL_ENTITY_METHOD("StatefulEntityMethod");

  public final String filename;

  Template(final String filename) {
    this.filename = filename;
  }

}
