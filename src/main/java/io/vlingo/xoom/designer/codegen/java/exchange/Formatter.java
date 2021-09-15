// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

public class Formatter {

  public static String formatExchangeVariableName(final String exchangeName) {
    boolean shouldUpper = false;
    final StringBuilder formatted = new StringBuilder();
    for (char character : exchangeName.toLowerCase().toCharArray()) {
      if (!Character.isJavaIdentifierPart(character)) {
        shouldUpper = true;
        continue;
      }
      formatted.append(shouldUpper ? String.valueOf(character).toUpperCase() : character);
      shouldUpper = false;
    }
    return formatted.toString();
  }

  public static String formatSchemaTypeName(final CodeGenerationParameter schema) {
    return schema.value.split(":")[3];
  }

  public static String formatReceiverInnerClassName(final CodeGenerationParameter schema) {
    return formatSchemaTypeName(schema) + "Receiver";
  }

}
