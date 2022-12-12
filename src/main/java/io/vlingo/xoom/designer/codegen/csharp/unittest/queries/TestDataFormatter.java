// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.NumberFormat;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class TestDataFormatter {

  public static String formatStaticVariableName(final int dataIndex, final String methodName) {
    final String dataOrdinalIndex = NumberFormat.toOrdinal(dataIndex);
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final String formattedMethodName = codeElementFormatter.staticConstant(methodName);
    return String.format("%s_%s_TEST_DATA", dataOrdinalIndex, formattedMethodName).toUpperCase();
  }

  public static String formatLocalVariableName(final int dataIndex) {
    return NumberFormat.toOrdinal(dataIndex) + "Data";
  }
}
