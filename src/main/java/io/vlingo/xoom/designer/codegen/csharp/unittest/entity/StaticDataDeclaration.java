// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class StaticDataDeclaration {

  private static final String TEST_DATA_DECLARATION_PATTERN = "private static %s %s = %s;";

  public static List<String> generate(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                      final TestDataValueGenerator.TestDataValues initialTestDataValues) {
    return generate(method, aggregate, initialTestDataValues, null);
  }

  public static List<String> generate(final CodeGenerationParameter method,
                                      final CodeGenerationParameter aggregate,
                                      final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                      final TestDataValueGenerator.TestDataValues updatedTestDataValues) {
    final TestDataValueGenerator.TestDataValues currentTestDataValues =
            method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf) ? initialTestDataValues : updatedTestDataValues;


    return AggregateDetail.findInvolvedStateFields(aggregate, method.value, Tuple2::from)
            .map(tuple -> testDataDeclaration(method, aggregate, currentTestDataValues, tuple))
        .collect(Collectors.toList());
  }

  private static String testDataDeclaration(CodeGenerationParameter method, CodeGenerationParameter aggregate,
                                            TestDataValueGenerator.TestDataValues currentTestDataValues,
                                            Tuple2<CodeGenerationParameter, CodeGenerationParameter> tuple) {
    final CodeGenerationParameter methodParameter = tuple._1;
    final CodeGenerationParameter stateField = tuple._2;

    final CollectionMutation collectionMutation =
            methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

    final String stateFieldType = FieldDetail.typeOf(aggregate, stateField.value, collectionMutation);

    final String testDataVariableName =  TestDataFormatter.formatStaticVariableName(method, stateField);

    final String dataInstantiation = InlineDataInstantiationFormatter
        .with(methodParameter, stateField, currentTestDataValues).format();

    return String.format(TEST_DATA_DECLARATION_PATTERN, stateFieldType, testDataVariableName, dataInstantiation);
  }

}
