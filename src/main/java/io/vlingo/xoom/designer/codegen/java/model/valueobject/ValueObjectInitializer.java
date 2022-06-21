// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model.valueobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;

  public ValueObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent,
                             final Stream<CodeGenerationParameter> valueObjectsStream) {
    final List<CodeGenerationParameter> valueObjects = valueObjectsStream.collect(Collectors.toList());
    if(hasMultiNestedValueObjects(findInvolvedFields(parent).collect(Collectors.toList()), valueObjects))
      return findInvolvedFields(parent).filter(ValueObjectDetail::isValueObject)
              .flatMap(field -> buildExpressionsForMultiNestedValueObjects(field, valueObjects).stream())
              .collect(Collectors.toList());

    return findInvolvedFields(parent).filter(ValueObjectDetail::isValueObject)
        .flatMap(field -> buildExpressions(field, valueObjects).stream())
        .collect(Collectors.toList());
  }

  private boolean hasMultiNestedValueObjects(List<CodeGenerationParameter> involvedFields, List<CodeGenerationParameter> valueObjects) {
    return involvedFields.stream()
            .filter(ValueObjectDetail::isValueObject)
            .map(field -> ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream()))
            .flatMap(vo -> vo.retrieveAllRelated(Label.VALUE_OBJECT_FIELD))
            .map(vo->vo.value)
            .collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum))
            .values().stream().anyMatch(count -> count > 1);
  }

  private Stream<CodeGenerationParameter> findInvolvedFields(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE_METHOD)) {
      return AggregateDetail.findInvolvedStateFields(parent.parent(), parent.value);
    }
    if (parent.isLabeled(Label.AGGREGATE)) {
      return parent.retrieveAllRelated(Label.STATE_FIELD);
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD);
    }
    throw new UnsupportedOperationException("Unable to format " + parent.label);
  }

  private List<String> buildExpressions(final CodeGenerationParameter stateField,
                                        final List<CodeGenerationParameter> valueObjects) {
    final List<String> expressions = new ArrayList<>();
    buildExpression(carrierName, stateField, valueObjects, expressions);
    return expressions;
  }

  private List<String> buildExpressionsForMultiNestedValueObjects(final CodeGenerationParameter stateField,
                                        final List<CodeGenerationParameter> valueObjects) {
    final List<String> expressions = new ArrayList<>();
    buildExpressionForMultiNestedValueObjects(carrierName, stateField, valueObjects, expressions);
    return expressions;
  }

  private void buildExpressionForMultiNestedValueObjects(final String carrierReferencePath,
                               final CodeGenerationParameter field,
                               final List<CodeGenerationParameter> valueObjects,
                               final List<String> expressions) {
    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

    final String fieldReferencePath =
            String.format("%s.%s", carrierReferencePath, field.value);

    final String args =
            valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
                    .map(valueObjectField -> resolveArgumentForMultiNestedValueObjects(fieldReferencePath, valueObjectField, valueObjects))
                    .collect(Collectors.joining(", "));

    final String expression =
            String.format("final %s %s = %s.from(%s);", valueObject.value, field.value, valueObject.value, args);

    expressions.add(expression);
  }

  private void buildExpression(final String carrierReferencePath,
                               final CodeGenerationParameter field,
                               final List<CodeGenerationParameter> valueObjects,
                               final List<String> expressions) {
    final CodeGenerationParameter valueObject =
        ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

    final String fieldReferencePath =
        String.format("%s.%s", carrierReferencePath, field.value);

    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .filter(ValueObjectDetail::isValueObject)
        .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

    final String args =
        valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .map(valueObjectField -> resolveArgument(fieldReferencePath, valueObjectField, valueObjects))
            .collect(Collectors.joining(", "));

    final String expression =
        String.format("final %s %s = %s.from(%s);", valueObject.value, field.value, valueObject.value, args);

    expressions.add(expression);
  }

  private String resolveArgument(final String fieldReferencePath,
                                 final CodeGenerationParameter valueObjectField, List<CodeGenerationParameter> valueObjects) {
    final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
    if (ValueObjectDetail.isValueObject(valueObjectField)) {
      return valueObjectField.value;
    }
    if (FieldDetail.isValueObjectCollection(valueObjectField)) {
      final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);
      final String collectionType = valueObjectField.retrieveRelatedValue(Label.COLLECTION_TYPE);

      if (FieldDetail.isValueObjectCollection(valueObjectField.parent().retrieveOneRelated(Label.VALUE_OBJECT_FIELD))) {
        final String dataObjectFieldName = valueObjectField.parent().retrieveRelatedValue(Label.VALUE_OBJECT_FIELD);
        return String.format("%s.%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())", fieldReferencePath, dataObjectFieldName, dataObjectName, fieldType, collectionType);
      }

      return String.format("%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())", fieldReferencePath, dataObjectName, fieldType, collectionType);
    }
    return String.format("%s.%s", fieldReferencePath, valueObjectField.value);
  }

  private String resolveArgumentForMultiNestedValueObjects(final String fieldReferencePath,
                                 final CodeGenerationParameter valueObjectField, List<CodeGenerationParameter> valueObjects) {
    final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
    if (ValueObjectDetail.isValueObject(valueObjectField)) {
      final String args = ValueObjectDetail.valueObjectOf(valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream())
              .retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
              .map(valueObject -> String.format("%s.%s.%s", fieldReferencePath, valueObjectField.value, valueObject.value))
              .collect(Collectors.joining(", "));
      return String.format("%s.from(%s)", fieldType, args);
    }
    if (FieldDetail.isValueObjectCollection(valueObjectField)) {
      final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);
      final String collectionType = valueObjectField.retrieveRelatedValue(Label.COLLECTION_TYPE);

      if (FieldDetail.isValueObjectCollection(valueObjectField.parent().retrieveOneRelated(Label.VALUE_OBJECT_FIELD))) {
        final String dataObjectFieldName = valueObjectField.parent().retrieveRelatedValue(Label.VALUE_OBJECT_FIELD);
        return String.format("%s.%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())", fieldReferencePath, dataObjectFieldName, dataObjectName, fieldType, collectionType);
      }

      return String.format("%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())", fieldReferencePath, dataObjectName, fieldType, collectionType);
    }
    return String.format("%s.%s", fieldReferencePath, valueObjectField.value);
  }

}
