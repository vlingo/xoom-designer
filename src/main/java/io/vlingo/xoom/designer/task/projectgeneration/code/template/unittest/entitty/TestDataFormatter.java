// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

public class TestDataFormatter {

  public static String formatStaticVariableName(final CodeGenerationParameter method,
                                                final CodeGenerationParameter stateField) {
    final String formattedMethodName = CodeElementFormatter.staticConstant(method.value);
    final String formattedStateFieldName = CodeElementFormatter.staticConstant(stateField.value);
    return String.format("%s_FOR_%s_TEST", formattedStateFieldName, formattedMethodName).toUpperCase();
  }

}
