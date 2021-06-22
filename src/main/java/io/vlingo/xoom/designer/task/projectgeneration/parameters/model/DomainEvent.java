// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.model;

import java.util.ArrayList;
import java.util.List;

public class DomainEvent {

  private final String name;
  private final Method method;
  private final List<Field> stateFields = new ArrayList<>();

  public DomainEvent(final String name,
                     final Method method,
                     final List<Field> stateFields) {
    this.name = name;
    this.method = method;
    this.stateFields.addAll(stateFields);
  }

}
