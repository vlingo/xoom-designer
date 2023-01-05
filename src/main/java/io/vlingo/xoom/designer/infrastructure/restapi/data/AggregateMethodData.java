// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.List;

public class AggregateMethodData {

  public final String name;
  public final List<MethodParameterData> parameters;
  public final Boolean useFactory;
  public final String event;

  public AggregateMethodData(final String name,
                             final List<MethodParameterData> parameters,
                             final Boolean useFactory,
                             final String event) {
    this.name = name;
    this.event = event;
    this.useFactory = useFactory;
    this.parameters = parameters;
  }

  public List<String> validate(List<String> errorStrings) {
    if (name == null) errorStrings.add("AggregateMethodData.name is null");
    if (parameters == null) errorStrings.add("AggregateMethodData.parameters is null");
    if (useFactory == null) errorStrings.add("AggregateMethodData.factory is null");
    return errorStrings;
  }
}