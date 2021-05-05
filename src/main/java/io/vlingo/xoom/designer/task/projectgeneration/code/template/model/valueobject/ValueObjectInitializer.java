// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject;

import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class ValueObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;

  public ValueObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter method,
                             final Stream<CodeGenerationParameter> fields) {
    final CodeGenerationParameter aggregate = method.parent(AGGREGATE);
    final List<CodeGenerationParameter> valueObjects = fields.collect(Collectors.toList());
    return method.retrieveAllRelated(METHOD_PARAMETER)
            .map(param -> AggregateDetail.stateFieldWithName(aggregate, param.value))
            .filter(ValueObjectDetail::isValueObject)
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
    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(field.retrieveRelatedValue(FIELD_TYPE), valueObjects.stream());

    final String fieldReferencePath =
            String.format("%s.%s", carrierReferencePath, field.value);

    valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .filter(ValueObjectDetail::isValueObject)
            .forEach(valueObjectField -> buildExpression(fieldReferencePath, valueObjectField, valueObjects, expressions));

    final Function<CodeGenerationParameter, String> pathResolver = valueObjectField ->
            ValueObjectDetail.isValueObject(valueObjectField) ? valueObjectField.value :
                    String.format("%s.%s", fieldReferencePath, valueObjectField.value);

    final String args =
            valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD).map(pathResolver).collect(Collectors.joining(", "));

    final String expression =
            String.format("final %s %s = %s.from(%s);", valueObject.value, field.value, valueObject.value, args);

    expressions.add(expression);
  }

}
