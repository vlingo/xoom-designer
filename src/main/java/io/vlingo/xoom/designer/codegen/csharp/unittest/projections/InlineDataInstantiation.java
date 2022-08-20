// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.NumberFormat;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.List;
import java.util.function.Consumer;

public class InlineDataInstantiation {

  private final int dataIndex;
  @SuppressWarnings("unused")
  private final String ordinalIndex;
  private final TemplateStandard standard;
  private final CodeGenerationParameter aggregate;
  private final List<CodeGenerationParameter> valueObjects;
  private final StringBuilder valuesAssignmentExpression;
  private final TestDataValueGenerator.TestDataValues testDataValues;

  public static InlineDataInstantiation with(final int dataIndex,
                                             final TemplateStandard standard,
                                             final CodeGenerationParameter aggregate,
                                             final List<CodeGenerationParameter> valueObjects,
                                             final TestDataValueGenerator.TestDataValues testDataValues) {
    return new InlineDataInstantiation(dataIndex, standard, aggregate, valueObjects, testDataValues);
  }

  private InlineDataInstantiation(final int dataIndex,
                                  final TemplateStandard standard,
                                  final CodeGenerationParameter aggregate,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final TestDataValueGenerator.TestDataValues testDataValues) {
    this.dataIndex = dataIndex;
    this.standard = standard;
    this.aggregate = aggregate;
    this.valueObjects = valueObjects;
    this.ordinalIndex = NumberFormat.toOrdinal(dataIndex);
    this.valuesAssignmentExpression = new StringBuilder();
    this.testDataValues = testDataValues;
  }

  public String generate() {
    generateFieldsAssignment();
    return String.format("%s.From(%s)", standard.resolveClassname(aggregate.value),
        valuesAssignmentExpression.toString()).replaceAll(", \\)", ")");
  }

  private void generateFieldsAssignment() {
    aggregate.retrieveAllRelated(Label.STATE_FIELD).forEach(field -> this.generateFieldAssignment("", field));
  }

  private void generateFieldAssignment(final String fieldPath, final CodeGenerationParameter field) {
    final String currentFieldPath = fieldPath.isEmpty() ? field.value : fieldPath + "." + field.value;
    if (ValueObjectDetail.isValueObject(field)) {
      generateValueObjectAssignment(currentFieldPath, field);
    } else if (FieldDetail.isCollection(field) || FieldDetail.isDateTime(field)) {
      final String defaultValue = FieldDetail.resolveDefaultValue(field.parent(), field.value);
      valuesAssignmentExpression.append(defaultValue).append(", ");
    } else {
      valuesAssignmentExpression.append(testDataValues.retrieve(dataIndex, currentFieldPath)).append(", ");
    }
  }

  private void generateValueObjectAssignment(final String fieldPath, final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);

    final CodeGenerationParameter valueObject = ValueObjectDetail.valueObjectOf(fieldType, valueObjects.stream());

    final Consumer<CodeGenerationParameter> valueObjectFieldAssignment =
        valueObjectField -> generateFieldAssignment(fieldPath, valueObjectField);

    valuesAssignmentExpression.append(standard.resolveClassname(fieldType)).append(".From(");
    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(valueObjectFieldAssignment);
    valuesAssignmentExpression.append("), ");
  }
}
