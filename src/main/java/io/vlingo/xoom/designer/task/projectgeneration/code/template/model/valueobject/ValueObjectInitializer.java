// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject;

import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class ValueObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;

  public ValueObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent,
                             final Stream<CodeGenerationParameter> valueObjectsStream) {
    final List<CodeGenerationParameter> valueObjects = valueObjectsStream.collect(Collectors.toList());
    return findInvolvedFields(parent)
            .filter(field -> ValueObjectDetail.isValueObject(field) || FieldDetail.isValueObjectCollection(field))
            .flatMap(field -> buildExpressions(field, valueObjects).stream())
            .collect(Collectors.toList());
  }

  private Stream<CodeGenerationParameter> findInvolvedFields(final CodeGenerationParameter parent) {
    if(parent.isLabeled(AGGREGATE_METHOD)) {
      return AggregateDetail.findInvolvedStateFields(parent.parent(), parent.value);
    }
    if(parent.isLabeled(AGGREGATE)) {
      return parent.retrieveAllRelated(STATE_FIELD);
    }
    if(parent.isLabeled(VALUE_OBJECT)) {
      return parent.retrieveAllRelated(VALUE_OBJECT_FIELD);
    }
    throw new UnsupportedOperationException("Unable to format " + parent.label);
  }

  private List<String> buildExpressions(final CodeGenerationParameter stateField,
                                        final List<CodeGenerationParameter> valueObjects) {
    final List<String> expressions = new ArrayList<>();
    buildExpression(carrierName, stateField, valueObjects, expressions);
    return expressions;
  }

  private void buildExpression(final String carrierReferencePath,
                               final CodeGenerationParameter field,
                               final List<CodeGenerationParameter> valueObjects,
                               final List<String> expressions) {
    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(FIELD_TYPE), valueObjects.stream());

    final String fieldReferencePath =
            String.format("%s.%s", carrierReferencePath, field.value);

    valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .filter(ValueObjectDetail::isValueObject)
            .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

    final String args =
            valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
                    .map(valueObjectField -> resolveArgument(fieldReferencePath, valueObjectField))
                    .collect(Collectors.joining(", "));

    final String expression =
            String.format("final %s %s = %s.from(%s);", valueObject.value, field.value, valueObject.value, args);

    expressions.add(expression);
  }

  private String resolveArgument(final String fieldReferencePath,
                                 final CodeGenerationParameter valueObjectField) {
    final String fieldType = valueObjectField.retrieveRelatedValue(FIELD_TYPE);
    if(ValueObjectDetail.isValueObject(valueObjectField)) {
      return valueObjectField.value;
    }
    if(FieldDetail.isValueObjectCollection(valueObjectField) && !FieldDetail.isScalar(fieldType)) {
      final String dataObjectName = DesignerTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);
      final String collectionType = valueObjectField.retrieveRelatedValue(COLLECTION_TYPE);
      return String.format("%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())", fieldReferencePath, dataObjectName, fieldType, collectionType);
    }
    return String.format("%s.%s", fieldReferencePath, valueObjectField.value);
  }

}
