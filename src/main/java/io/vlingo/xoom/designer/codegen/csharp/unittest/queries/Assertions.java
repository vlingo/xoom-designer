// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Assertions {

  private static final String ASSERT_NOT_NULL_PLACEHOLDER = "Assert.NotNull(%s);";
  private static final String ASSERT_EQUAL_PLACEHOLDER = "Assert.Equal(%s, %s);";

  public static List<String> from(final int dataIndex, final CodeGenerationParameter aggregate,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final TestDataValueGenerator.TestDataValues testDataValues) {
    final String variableName = TestDataFormatter.formatLocalVariableName(dataIndex);

    final Function<String, String> toPascalCaseMapper = path -> Arrays.stream(path.split("\\."))
        .skip(1)
        .map(FieldDetail::toPascalCase).collect(Collectors.joining("."));
    final List<String> fieldPaths = AggregateDetail.resolveFieldsPaths(variableName, aggregate.retrieveAllRelated(Label.STATE_FIELD), valueObjects);

    final Function<String, String> mapper =
        fieldPath -> {
          final String fieldType = AggregateDetail.stateFieldType(aggregate, fieldPath, valueObjects);
          if (FieldDetail.isCollection(fieldType) || FieldDetail.isDateTime(fieldType)) {
            return String.format(ASSERT_NOT_NULL_PLACEHOLDER, fieldPath.split("\\.")[0] + "."+toPascalCaseMapper.apply(fieldPath));
          }
          return String.format(ASSERT_EQUAL_PLACEHOLDER, testDataValues.retrieve(dataIndex, variableName, fieldPath), fieldPath.split("\\.")[0] + "."+toPascalCaseMapper.apply(fieldPath));
        };

    return fieldPaths.stream().map(mapper).collect(Collectors.toList());
  }

}
