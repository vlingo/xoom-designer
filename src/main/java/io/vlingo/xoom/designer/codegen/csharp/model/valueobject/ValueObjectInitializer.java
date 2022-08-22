// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.valueobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toCamelCase;
import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class ValueObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;
  private ValueObjectInitializer initializer;
  private static final String VALUE_OBJECT_INSTANTIATION = "var %s = %s.From(%s);";
  private static final String VALUE_OBJECT_FACTORY_METHOD = "%s.From(%s)";

  public ValueObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent, final Stream<CodeGenerationParameter> valueObjectsStream) {
    final List<CodeGenerationParameter> valueObjects = valueObjectsStream.collect(Collectors.toList());
    Function<CodeGenerationParameter, Stream<? extends String>> mapper =
        field -> buildExpressions(field, valueObjects).stream();

    if (hasMultiNestedValueObjects(findInvolvedFields(parent).collect(Collectors.toList()), valueObjects))
      mapper = field -> initializer.buildExpressions(field, valueObjects).stream();

    return findInvolvedFields(parent).filter(ValueObjectDetail::isValueObject)
        .flatMap(mapper)
        .collect(Collectors.toList());
  }

  protected boolean hasMultiNestedValueObjects(final List<CodeGenerationParameter> involvedFields, final List<CodeGenerationParameter> valueObjects) {
    final List<CodeGenerationParameter> parameters = involvedFields.stream()
        .filter(ValueObjectDetail::isValueObject)
        .map(field -> ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream()))
        .flatMap(vo -> vo.retrieveAllRelated(Label.VALUE_OBJECT_FIELD))
        .collect(Collectors.toList());

    return stateFieldHasMultiNestedValueObjects(parameters, valueObjects) ||
        containsDuplication(parameters.stream().map(vo -> vo.value));
  }

  private boolean containsDuplication(Stream<String> stringStream) {
    return stringStream.collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum))
        .values().stream().anyMatch(count -> count > 1);
  }

  private boolean stateFieldHasMultiNestedValueObjects(List<CodeGenerationParameter> involvedFields, List<CodeGenerationParameter> valueObjects) {
    return containsDuplication(involvedFields.stream()
        .filter(ValueObjectDetail::isValueObject)
        .map(field -> ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream()))
        .flatMap(vo -> vo.retrieveAllRelated(Label.VALUE_OBJECT_FIELD))
        .map(vo -> vo.value));
  }

  private Stream<CodeGenerationParameter> findInvolvedFields(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE_METHOD)) {
      initializer = new StateFieldValueObjectInitializer(carrierName);
      return AggregateDetail.findInvolvedStateFields(parent.parent(), parent.value);
    }
    if (parent.isLabeled(Label.AGGREGATE)) {
      initializer = new StateFieldValueObjectInitializer(carrierName);
      return parent.retrieveAllRelated(Label.STATE_FIELD);
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      initializer = new NestedValueObjectInitializer(carrierName);
      return parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD);
    }
    throw new UnsupportedOperationException("Unable to format " + parent.label);
  }

  protected List<String> buildExpressions(final CodeGenerationParameter stateField,
                                          final List<CodeGenerationParameter> valueObjects) {
    final List<String> expressions = new ArrayList<>();
    buildExpression(carrierName, stateField, valueObjects, expressions);
    return expressions;
  }

  protected void buildExpression(final String carrierReferencePath, final CodeGenerationParameter field,
                                 final List<CodeGenerationParameter> valueObjects, final List<String> expressions) {
    final CodeGenerationParameter valueObject =
        ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

    final String fieldReferencePath = String.format("%s.%s", carrierReferencePath, toPascalCase(field.value));

    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .filter(ValueObjectDetail::isValueObject)
        .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

    final String args = valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .map(valueObjectField -> resolveArgument(fieldReferencePath, valueObjectField, valueObjects))
            .collect(Collectors.joining(", "));

    final String expression = String.format(VALUE_OBJECT_INSTANTIATION, field.value, valueObject.value, args);

    expressions.add(expression);
  }

  protected String resolveArgument(final String fieldReferencePath, final CodeGenerationParameter valueObjectField,
                                   final List<CodeGenerationParameter> valueObjects) {
    if (ValueObjectDetail.isValueObject(valueObjectField)) {
      return toCamelCase(valueObjectField.value);
    }
    return String.format("%s.%s", fieldReferencePath, toPascalCase(valueObjectField.value));
  }

  private static class NestedValueObjectInitializer extends ValueObjectInitializer {
    public NestedValueObjectInitializer(String carrierName) {
      super(carrierName);
    }

    protected void buildExpression(final String carrierReferencePath, final CodeGenerationParameter field,
                                   final List<CodeGenerationParameter> valueObjects, final List<String> expressions) {
      final CodeGenerationParameter valueObject =
          ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

      final String fieldReferencePath = String.format("%s.%s", carrierReferencePath, toPascalCase(field.value));

      final String args = valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
              .map(valueObjectField -> resolveArgument(fieldReferencePath, valueObjectField, valueObjects))
              .collect(Collectors.joining(", "));

      final String expression = String.format(VALUE_OBJECT_INSTANTIATION, field.value, valueObject.value, args);

      expressions.add(expression);
    }

    protected String resolveArgument(final String fieldReferencePath, final CodeGenerationParameter valueObjectField,
                                     final List<CodeGenerationParameter> valueObjects) {
      final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
      if (ValueObjectDetail.isValueObject(valueObjectField)) {
        final String args = ValueObjectDetail.valueObjectOf(valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream())
            .retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .map(valueObject -> String.format("%s.%s.%s", fieldReferencePath, toPascalCase(valueObjectField.value), toPascalCase(valueObject.value)))
            .collect(Collectors.joining(", "));
        return String.format(VALUE_OBJECT_FACTORY_METHOD, fieldType, args);
      }

      return super.resolveArgument(fieldReferencePath, valueObjectField, valueObjects);
    }

  }

  private static class StateFieldValueObjectInitializer extends ValueObjectInitializer {
    public StateFieldValueObjectInitializer(String carrierName) {
      super(carrierName);
    }

    protected void buildExpression(final String carrierReferencePath, final CodeGenerationParameter field,
                                   final List<CodeGenerationParameter> valueObjects, final List<String> expressions) {
      final CodeGenerationParameter valueObject =
          ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

      final String fieldReferencePath = String.format("%s.%s", carrierReferencePath, toPascalCase(field.value));
      if (hasMultiNestedValueObjects(valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).collect(Collectors.toList()), valueObjects) ||
          FieldDetail.isValueObjectCollection(field))
        valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .filter(ValueObjectDetail::isValueObject)
            .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

      final String args = valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
              .map(valueObjectField -> resolveArgument(fieldReferencePath, valueObjectField, valueObjects))
              .collect(Collectors.joining(", "));

      final String expression = String.format(VALUE_OBJECT_INSTANTIATION, field.value, valueObject.value, args);

      expressions.add(expression);
    }

    protected String resolveArgument(final String fieldReferencePath, final CodeGenerationParameter valueObjectField,
                                     final List<CodeGenerationParameter> valueObjects) {
      final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
      if (ValueObjectDetail.isValueObject(valueObjectField)) {
        final CodeGenerationParameter valueObject = ValueObjectDetail.valueObjectOf(valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());
        if (valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).anyMatch(ValueObjectDetail::isValueObject)) {
          return valueObjectField.value;
        }
        Function<CodeGenerationParameter, String> mapper = vo ->
            String.format("%s.%s.%s", fieldReferencePath, toPascalCase(valueObjectField.value), toPascalCase(vo.value));

        if (valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).anyMatch(FieldDetail::isValueObjectCollection))
          mapper = vo -> super.resolveArgument(fieldReferencePath, vo, valueObjects);

        final String args = valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .map(mapper)
            .collect(Collectors.joining(", "));

        return String.format(VALUE_OBJECT_FACTORY_METHOD, fieldType, args);
      }

      return super.resolveArgument(fieldReferencePath, valueObjectField, valueObjects);
    }
  }
}
