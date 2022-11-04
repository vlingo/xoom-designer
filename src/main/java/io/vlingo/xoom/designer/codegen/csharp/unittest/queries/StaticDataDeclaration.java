// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;
import io.vlingo.xoom.designer.codegen.csharp.unittest.projections.InlineDataInstantiation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StaticDataDeclaration {

  private static final String TEST_DATA_DECLARATION_PATTERN = "private static %s %s = %s;";

  public static List<String> generate(final String methodName, final CodeGenerationParameter aggregate,
                                      final List<CodeGenerationParameter> valueObjects,
                                      final TestDataValueGenerator.TestDataValues testDataValues) {
    return IntStream.range(1, TestCase.TEST_DATA_SET_SIZE + 1).mapToObj(dataIndex -> {
      final String testDataVariableName = TestDataFormatter.formatStaticVariableName(dataIndex, methodName);

      final String dataObjectType = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

      final String dataInstantiation = InlineDataInstantiation.with(dataIndex, "Data", CsharpTemplateStandard.DATA_OBJECT, aggregate,
                      valueObjects, testDataValues).generate();

      return String.format(TEST_DATA_DECLARATION_PATTERN, dataObjectType, testDataVariableName, dataInstantiation);
    }).collect(Collectors.toList());
  }
}
