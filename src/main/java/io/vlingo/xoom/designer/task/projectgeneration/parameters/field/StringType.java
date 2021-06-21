// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

public class StringType extends ScalarType {

  protected StringType() {
    super("String");
  }

  public static boolean isString(final String typeName) {
    return typeName.equalsIgnoreCase("String");
  }

  @Override
  public boolean isNumeric() {
    return false;
  }

  @Override
  public boolean isString() {
    return true;
  }

  @Override
  public String defaultValue() {
    return "null";
  }

}
