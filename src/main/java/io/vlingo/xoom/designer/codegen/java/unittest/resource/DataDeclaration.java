// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.List;

public class DataDeclaration {

  private static final String TEST_DATA_DECLARATION_PATTERN = "%s %s = %s;";

  public static String generate(final String methodName,
                                final CodeGenerationParameter aggregate,
                                final List<CodeGenerationParameter> valueObjects,
                                final TestDataValueGenerator.TestDataValues testDataValues) {
    final String testDataVariableName =
            TestDataFormatter.formatLocalVariableName(1);

    final String dataObjectType =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

    final String dataInstantiation =
        InlineDataInstantiation.with(1, JavaTemplateStandard.DATA_OBJECT, aggregate,
            valueObjects, testDataValues).generate();

    return String.format(TEST_DATA_DECLARATION_PATTERN, dataObjectType, testDataVariableName, dataInstantiation);
  }
}
