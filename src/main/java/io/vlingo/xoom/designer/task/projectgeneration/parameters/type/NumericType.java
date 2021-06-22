// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.type;

import io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties;

public class NumericType extends ScalarType {

  public static NumericType withName(final String typeName) {
    return new NumericType(typeName);
  }

  private NumericType(final String typeName) {
    super(typeName);
  }

  @Override
  public boolean isNumeric() {
    return true;
  }

  @Override
  public boolean isString() {
    return false;
  }

  @Override
  public String defaultValue() {
    return "0";
  }

  public static boolean isNumeric(final String type) {
    return CodeGenerationProperties.SCALAR_NUMERIC_TYPES.contains(type);
  }
}
