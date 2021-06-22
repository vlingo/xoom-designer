// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.model;

import io.vlingo.xoom.designer.task.projectgeneration.parameters.type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {

  private final String name;
  private final Type type;
  private final List<Field> nestedFields = new ArrayList<>();

  public Field of(final String name, final Type type) {
    return new Field(name, type, Collections.emptyList());
  }

  public Field of(final String name,
                  final Type type,
                  final List<Field> nestedFields) {
    return new Field(name, type, nestedFields);
  }

  private Field(final String name, final Type type, final List<Field> nestedFields) {
    this.name = name;
    this.type = type;
    this.nestedFields.addAll(nestedFields);
  }

  public String name() {
    return name;
  }

}
