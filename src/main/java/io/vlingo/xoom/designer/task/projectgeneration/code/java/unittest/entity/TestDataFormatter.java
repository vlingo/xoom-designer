// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.entity;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class TestDataFormatter {

  public static String formatStaticVariableName(final CodeGenerationParameter method,
                                                final CodeGenerationParameter stateField) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    final String formattedMethodName = codeElementFormatter.staticConstant(method.value);
    final String formattedStateFieldName = codeElementFormatter.staticConstant(stateField.value);
    return String.format("%s_FOR_%s_TEST", formattedStateFieldName, formattedMethodName).toUpperCase();
  }

}
