// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.entitty;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.TestDataValueGenerator.TestDataValues;

import java.util.List;
import java.util.function.Consumer;

public class InlineDataInstantiationFormatter {

  private final CodeGenerationParameter stateField;
  private final List<CodeGenerationParameter> valueObjects;
  private final StringBuilder valuesAssignmentExpression;
  private final TestDataValues testDataValues;

  public static InlineDataInstantiationFormatter with(final CodeGenerationParameter stateField,
                                                      final List<CodeGenerationParameter> valueObjects,
                                                      final TestDataValues testDataValues) {
    return new InlineDataInstantiationFormatter(stateField, valueObjects, testDataValues);
  }

  private InlineDataInstantiationFormatter(final CodeGenerationParameter stateField,
                                           final List<CodeGenerationParameter> valueObjects,
                                           final TestDataValues testDataValues) {
    this.stateField = stateField;
    this.valueObjects = valueObjects;
    this.valuesAssignmentExpression = new StringBuilder();
    this.testDataValues = testDataValues;
  }

  public String format() {
    if (FieldDetail.isScalar(stateField)) {
      return formatScalarTypedField();
    }
    return formatComplexTypedField();
  }

  private String formatScalarTypedField() {
    return testDataValues.retrieve(stateField.value);
  }

  private String formatComplexTypedField() {
    if(FieldDetail.isCollectionOrDate(stateField)) {
      return FieldDetail.resolveDefaultValue(stateField.parent(), stateField.value);
    }

    final String valueObjectType =
            stateField.retrieveRelatedValue(Label.FIELD_TYPE);

    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(valueObjectType, valueObjects.stream());

    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(field -> generateValueObjectFieldAssignment(stateField.value, field));

    return String.format("%s.from(%s)", valueObjectType, valuesAssignmentExpression.toString()).replaceAll(", \\)", ")");
  }

  private void generateValueObjectFieldAssignment(final String path, final CodeGenerationParameter field) {
    final String currentFieldPath = path + "." + field.value;
    if(FieldDetail.isCollectionOrDate(stateField)) {
      valuesAssignmentExpression.append(FieldDetail.resolveDefaultValue(stateField.parent(), stateField.value)).append(", ");
    } else if (ValueObjectDetail.isValueObject(field)) {
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

    valuesAssignmentExpression.append(fieldType).append(".from(");
    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(valueObjectFieldAssignment);
    valuesAssignmentExpression.append("), ");
  }

}
