// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.projections;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.TestDataValueGenerator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Assertions {

  public static List<String> from(final int dataIndex,
                                  final CodeGenerationParameter aggregate,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final TestDataValueGenerator.TestDataValues testDataValues) {
    final String variableName = "item";//TestDataFormatter.formatLocalVariableName(dataIndex);

    final List<String> fieldPaths = AggregateDetail.resolveFieldsPaths(variableName, aggregate, valueObjects);

    final Function<String, String> mapper =
        fieldPath -> {
          final String fieldType = AggregateDetail.stateFieldType(aggregate, fieldPath, valueObjects);
          if (FieldDetail.isCollection(fieldType) || FieldDetail.isDateTime(fieldType)) {
            return String.format("assertNotNull(%s);", fieldPath);
          }
          return String.format("assertEquals(%s, %s);", fieldPath, testDataValues.retrieve(dataIndex, variableName, fieldPath));
        };

    return fieldPaths.stream().map(mapper).collect(Collectors.toList());
  }

}
