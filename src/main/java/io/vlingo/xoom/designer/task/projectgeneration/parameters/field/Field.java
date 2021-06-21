// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

public class Field {

  private final String name;
  private final Type type;

  public Field of(final String name, final Type type) {
    return new Field(name, type);
  }

  private Field(final String name, final Type type) {
    this.name = name;
    this.type = type;
  }

}
