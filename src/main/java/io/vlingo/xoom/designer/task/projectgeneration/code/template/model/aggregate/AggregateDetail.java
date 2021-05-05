// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class AggregateDetail {

  public static String resolvePackage(final String basePackage, final String aggregateProtocolName) {
    return String.format("%s.%s.%s", basePackage, "model", aggregateProtocolName).toLowerCase();
  }

  public static CodeGenerationParameter stateFieldWithName(final CodeGenerationParameter aggregate, final String fieldName) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).filter(field -> field.value.equals(fieldName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found"));
  }

  public static CodeGenerationParameter methodWithName(final CodeGenerationParameter aggregate, final String methodName) {
    return findMethod(aggregate, methodName).orElseThrow(() -> new IllegalArgumentException("Method " + methodName + " not found"));
  }

  public static CodeGenerationParameter eventWithName(final CodeGenerationParameter aggregate, final String eventName) {
    if(eventName == null || eventName.isEmpty()) {
      return CodeGenerationParameter.of(DOMAIN_EVENT, "");
    }
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).filter(event -> event.value.equals(eventName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Event " + eventName + " not found"));
  }

  public static String stateFieldType(final CodeGenerationParameter aggregate,
                                      final String fieldPath,
                                      final List<CodeGenerationParameter> valueObjects) {
    return stateFieldAtPath(0, aggregate, fieldPath.split("."), valueObjects);
  }

  private static String stateFieldAtPath(final int pathIndex,
                                         final CodeGenerationParameter parent,
                                         final String[] fieldPathParts,
                                         final List<CodeGenerationParameter> valueObjects) {
    final String fieldName = fieldPathParts[pathIndex];
    final CodeGenerationParameter field =
            parent.isLabeled(AGGREGATE) ? stateFieldWithName(parent, fieldName) :
                    ValueObjectDetail.valueObjectFieldWithName(parent, fieldName);

    final String fieldType = field.retrieveRelatedValue(FIELD_TYPE);

    if (pathIndex == fieldPathParts.length - 1) {
      return fieldType;
    }

    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(fieldType, valueObjects.stream());

    return stateFieldAtPath(pathIndex + 1, valueObject, fieldPathParts, valueObjects);
  }

  public static List<String> resolveFieldsPaths(final CodeGenerationParameter aggregate,
                                                final List<CodeGenerationParameter> valueObjects) {
    return resolveFieldsPaths("", aggregate.retrieveAllRelated(STATE_FIELD), valueObjects);
  }

  public static List<String> resolveFieldsPaths(final String variableName,
                                                final CodeGenerationParameter aggregate,
                                                final List<CodeGenerationParameter> valueObjects) {
    return resolveFieldsPaths(variableName, aggregate.retrieveAllRelated(STATE_FIELD), valueObjects);
  }

  public static List<String> resolveFieldsPaths(final String variableName,
                                                final Stream<CodeGenerationParameter> aggregateFields,
                                                final List<CodeGenerationParameter> valueObjects) {
    final List<String> paths = new ArrayList<>();
    aggregateFields.forEach(field -> resolveFieldPath(variableName, field, valueObjects, paths));
    return paths;
  }

  private static void resolveFieldPath(final String relativePath,
                                       final CodeGenerationParameter field,
                                       final List<CodeGenerationParameter> valueObjects,
                                       final List<String> paths) {
    final String currentRelativePath =
            relativePath.isEmpty() ? field.value : relativePath + "." + field.value;

    if (FieldDetail.isScalar(field)) {
      paths.add(currentRelativePath);
    } else if (ValueObjectDetail.isValueObject(field)) {
      final String valueObjectType =
              field.retrieveRelatedValue(FIELD_TYPE);

      final CodeGenerationParameter valueObject =
              ValueObjectDetail.valueObjectOf(valueObjectType, valueObjects.stream());

      valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
              .forEach(voField -> resolveFieldPath(currentRelativePath, voField, valueObjects, paths));
    }
  }

  public static Stream<CodeGenerationParameter> findInvolvedStateFields(final CodeGenerationParameter aggregate, final String methodName) {
    final CodeGenerationParameter method = methodWithName(aggregate, methodName);
    final Stream<CodeGenerationParameter> methodParameters = method.retrieveAllRelated(METHOD_PARAMETER);
    return methodParameters.map(parameter -> stateFieldWithName(aggregate, parameter.value));
  }

  private static Optional<CodeGenerationParameter> findMethod(final CodeGenerationParameter aggregate, final String methodName) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .filter(method -> methodName.equals(method.value) || method.value.startsWith(methodName + "("))
            .findFirst();
  }
}
