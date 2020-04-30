// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

public class AggregateGenerator extends BaseGenerator {
  public AggregateGenerator(final String aggregateProtocolName, final String stateClass) {
    super();

    input.put("aggregateProtocolName", aggregateProtocolName);
    input.put("stateClass", stateClass);
  }
}
