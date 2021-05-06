// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.TestDataValueGenerator.TestDataValues;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.FACTORY_METHOD;

public class StaticDataDeclaration {

  private static final String TEST_DATA_DECLARATION_PATTERN = "private static final %s %s = %s;";

  public static List<String> generate(final CodeGenerationParameter method,
                                      final CodeGenerationParameter aggregate,
                                      final List<CodeGenerationParameter> valueObjects,
                                      final TestDataValues initialTestDataValues) {
    return generate(method, aggregate, valueObjects, initialTestDataValues, null);
  }

  public static List<String> generate(final CodeGenerationParameter method,
                                      final CodeGenerationParameter aggregate,
                                      final List<CodeGenerationParameter> valueObjects,
                                      final TestDataValues initialTestDataValues,
                                      final TestDataValues updatedTestDataValues) {
    final TestDataValues currentTestDataValues =
            method.retrieveRelatedValue(FACTORY_METHOD, Boolean::valueOf) ? initialTestDataValues : updatedTestDataValues;

    return AggregateDetail.findInvolvedStateFields(aggregate, method.value).map(stateField -> {
      final String stateFieldType =
              FieldDetail.typeOf(aggregate, stateField.value);

      final String testDataVariableName =
              TestDataFormatter.formatStaticVariableName(method, stateField);

      final String dataInstantiation =
              InlineDataInstantiationFormatter.with(stateField, valueObjects, currentTestDataValues).format();

      return String.format(TEST_DATA_DECLARATION_PATTERN, stateFieldType, testDataVariableName, dataInstantiation);
    }).collect(Collectors.toList());
  }

}
