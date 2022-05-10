// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

public class InlineDataInstantiationFormatter {

  private final CodeGenerationParameter stateField;
  private final TestDataValueGenerator.TestDataValues testDataValues;
  private final CodeGenerationParameter methodParameter;

  public static InlineDataInstantiationFormatter with(final CodeGenerationParameter methodParameter,
                                                      final CodeGenerationParameter stateField,
                                                      final TestDataValueGenerator.TestDataValues testDataValues) {
    return new InlineDataInstantiationFormatter(methodParameter, stateField, testDataValues);
  }

  private InlineDataInstantiationFormatter(final CodeGenerationParameter methodParameter,
                                           final CodeGenerationParameter stateField,
                                           final TestDataValueGenerator.TestDataValues testDataValues) {
    this.stateField = stateField;
    this.testDataValues = testDataValues;
    this.methodParameter = methodParameter;
  }

  public String format() {
    if (FieldDetail.isMethodParameterAssignableToScalar(stateField, methodParameter)) {
      return formatScalarTypedField();
    }

    return formatComplexTypedField();
  }

  private String formatScalarTypedField() {
    return testDataValues.retrieve(stateField.value);
  }

  private String formatComplexTypedField() {
      return FieldDetail.resolveDefaultValue(stateField.parent(), stateField.value);
  }
}
