// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.model;

import java.util.ArrayList;
import java.util.List;

public class Method {

  private final boolean factory;
  private final List<MethodParameter> methodParameters = new ArrayList<>();
  private final DomainEvent event;

  public Method(final boolean factory,
                final List<MethodParameter> methodParameters,
                final DomainEvent event) {
    this.factory = factory;
    this.methodParameters.addAll(methodParameters);
    this.event = event;
  }

}
