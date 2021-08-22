// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.dataobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;
import static java.util.stream.Collectors.joining;

public class EventBasedDataObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;

  public EventBasedDataObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter event,
                             final Stream<CodeGenerationParameter> fields) {
    final List<CodeGenerationParameter> valueObjects = fields.collect(Collectors.toList());
    return event.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isAssignableToValueObject)
        .flatMap(field -> buildExpressions(field, valueObjects).stream())
        .collect(Collectors.toList());
  }

  private List<String> buildExpressions(final CodeGenerationParameter stateField, final List<CodeGenerationParameter> valueObjects) {
    final List<String> expressions = new ArrayList<>();
    buildExpression(carrierName, stateField, valueObjects, expressions);
    return expressions;
  }

  private void buildExpression(final String carrierReferencePath,
                               final CodeGenerationParameter field,
                               final List<CodeGenerationParameter> valueObjects,
                               final List<String> expressions) {
    final String fieldAlias =
        field.hasAny(ALIAS) ? field.retrieveRelatedValue(ALIAS) : field.value;

    final CodeGenerationParameter valueObject =
        ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());

    final CollectionMutation collectionMutation =
        field.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

    final String dataObjectName =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(valueObject.value);

    final String fieldReferencePath =
        String.format("%s.%s", carrierReferencePath, fieldAlias);

    valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
        .filter(ValueObjectDetail::isValueObject)
        .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

    final Function<CodeGenerationParameter, String> pathResolver = valueObjectField ->
        ValueObjectDetail.isValueObject(valueObjectField) ? valueObjectField.value :
            String.format("%s.%s", fieldReferencePath, valueObjectField.value);

    final String args =
        collectionMutation.isSingleParameterBased() ?
            fieldReferencePath : valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD).map(pathResolver).collect(joining(", "));
    if (FieldDetail.isValueObjectCollection(valueObject.retrieveOneRelated(Label.VALUE_OBJECT_FIELD))) {
      final String dataObjectMemberName =
          JavaTemplateStandard.DATA_OBJECT.resolveClassname(ValueObjectDetail.valueObjectOf(valueObject.retrieveOneRelated(Label.VALUE_OBJECT_FIELD).retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream()).value);
      final String expressionForAll =
          String.format("final %s %s = %s.from(%s.fromAll(%s));", dataObjectName, fieldAlias, dataObjectName, dataObjectMemberName, args);
      expressions.add(expressionForAll);
    } else {
      final String expression =
          String.format("final %s %s = %s.from(%s);", dataObjectName, fieldAlias, dataObjectName, args);
      expressions.add(expression);
    }
  }

}