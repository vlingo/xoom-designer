// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

import java.util.Collections;
import java.util.List;

public abstract class ScalarType implements Type {

  private final String typeName;

  public static ScalarType withName(final String typeName) {
    if(NumericType.isNumeric(typeName)) {
      return NumericType.withName(typeName);
    }
    if(BooleanType.isBoolean(typeName)) {
      return new BooleanType();
    }
    if(CharType.isChar(typeName)) {
      return new CharType();
    }
    if(StringType.isString(typeName)) {
      return new StringType();
    }
    throw new IllegalArgumentException("Cannot resolve scalar type: " + typeName);
  }

  protected ScalarType(final String typeName) {
    this.typeName = typeName;
  }

  public static boolean isScalar(final String typeName) {
    return NumericType.isNumeric(typeName) || BooleanType.isBoolean(typeName) ||
            CharType.isChar(typeName) || StringType.isString(typeName);
  }

  public String typeName() {
    return typeName;
  }

  @Override
  public List<String> qualifiedClassNames() {
    return Collections.emptyList();
  }

  @Override
  public boolean isScalar() {
    return true;
  }

}
