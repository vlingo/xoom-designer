// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

public class CharType extends ScalarType {

  CharType() {
    super("char");
  }

  @Override
  public boolean isNumeric() {
    return false;
  }

  @Override
  public boolean isString() {
    return false;
  }

  @Override
  public String defaultValue() {
    return "'\u0000'";
  }

  public static boolean isChar(final String typeName) {
    return typeName.equalsIgnoreCase("char");
  }

}
