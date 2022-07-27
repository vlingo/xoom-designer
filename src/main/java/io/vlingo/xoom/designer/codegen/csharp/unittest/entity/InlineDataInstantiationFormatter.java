// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.List;
import java.util.function.Consumer;

public class InlineDataInstantiationFormatter {

  private final CodeGenerationParameter stateField;
  private final TestDataValueGenerator.TestDataValues testDataValues;
  private final CodeGenerationParameter methodParameter;
  private final List<CodeGenerationParameter> valueObjects;
  private final StringBuilder valuesAssignmentExpression;

  public static InlineDataInstantiationFormatter with(final CodeGenerationParameter methodParameter,
                                                      final CodeGenerationParameter stateField,
                                                      final List<CodeGenerationParameter> valueObjects,
                                                      final TestDataValueGenerator.TestDataValues testDataValues) {
    return new InlineDataInstantiationFormatter(methodParameter, stateField, valueObjects, testDataValues);
  }

  private InlineDataInstantiationFormatter(final CodeGenerationParameter methodParameter,
                                           final CodeGenerationParameter stateField,
                                           final List<CodeGenerationParameter> valueObjects,
                                           final TestDataValueGenerator.TestDataValues testDataValues) {
    this.stateField = stateField;
    this.testDataValues = testDataValues;
    this.methodParameter = methodParameter;
    this.valueObjects = valueObjects;
    this.valuesAssignmentExpression = new StringBuilder();
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
    if(!FieldDetail.isMethodParameterAssignableToValueObject(stateField, methodParameter)) {
      return FieldDetail.resolveDefaultValue(stateField.parent(), stateField.value);
    }

    final String valueObjectType =
        stateField.retrieveRelatedValue(Label.FIELD_TYPE);

    final CodeGenerationParameter valueObject =
        ValueObjectDetail.valueObjectOf(valueObjectType, valueObjects.stream());

    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(field -> generateValueObjectFieldAssignment(stateField.value, field));

    return String.format("%s.From(%s)", valueObjectType, valuesAssignmentExpression).replaceAll(", \\)", ")");
  }

  private void generateValueObjectFieldAssignment(final String path, final CodeGenerationParameter field) {
    final String currentFieldPath = path + "." + field.value;
    if (FieldDetail.isCollectionOrDate(field)) {
      valuesAssignmentExpression.append(FieldDetail.resolveDefaultValue(field.parent(), field.value)).append(", ");
    } else if (FieldDetail.isMethodParameterAssignableToValueObject(field, methodParameter)) {
      generateComplexTypeAssignment(currentFieldPath, field);
    } else {
      generateScalarTypeAssignment(currentFieldPath);
    }
  }

  private void generateScalarTypeAssignment(final String fieldPath) {
    valuesAssignmentExpression.append(testDataValues.retrieve(fieldPath)).append(", ");
  }

  private void generateComplexTypeAssignment(final String fieldPath, final CodeGenerationParameter field) {
    final String fieldType =
        field.retrieveRelatedValue(Label.FIELD_TYPE);

    final CodeGenerationParameter valueObject =
        ValueObjectDetail.valueObjectOf(fieldType, valueObjects.stream());

    final Consumer<CodeGenerationParameter> valueObjectFieldAssignment =
        valueObjectField -> generateValueObjectFieldAssignment(fieldPath, valueObjectField);

    valuesAssignmentExpression.append(fieldType).append(".From(");
    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(valueObjectFieldAssignment);
    valuesAssignmentExpression.append("), ");
  }
}
