// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateDetail {

  private static final String COMPOSITE_ID_DECLARATION_PATTERN = "@Id final String";

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
      return CodeGenerationParameter.of(Label.DOMAIN_EVENT, "");
    }
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).filter(event -> event.value.equals(eventName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Event " + eventName + " not found"));
  }

  public static String stateFieldType(final CodeGenerationParameter aggregate,
                                      final String fieldPath,
                                      final List<CodeGenerationParameter> valueObjects) {
    return stateFieldAtPath(1, aggregate, fieldPath.split("\\."), valueObjects);
  }

  private static String stateFieldAtPath(final int pathIndex,
                                         final CodeGenerationParameter parent,
                                         final String[] fieldPathParts,
                                         final List<CodeGenerationParameter> valueObjects) {
    final String fieldName = fieldPathParts[pathIndex];
    final CodeGenerationParameter field =
            parent.isLabeled(Label.AGGREGATE) ? stateFieldWithName(parent, fieldName) :
                    ValueObjectDetail.valueObjectFieldWithName(parent, fieldName);

    final String fieldType = field.hasAny(Label.COLLECTION_TYPE) ? FieldDetail.typeOf(parent, field.value) : field.retrieveRelatedValue(Label.FIELD_TYPE);

    if (pathIndex == fieldPathParts.length - 1) {
      return fieldType;
    }

    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(fieldType, valueObjects.stream());

    return stateFieldAtPath(pathIndex + 1, valueObject, fieldPathParts, valueObjects);
  }

  public static Set<String> resolveImports(final CodeGenerationParameter aggregate) {
    return resolveImports(aggregate.retrieveAllRelated(Label.STATE_FIELD));
  }

  public static Set<String> resolveImports(final Stream<CodeGenerationParameter> stateFields) {
    return stateFields.map(FieldDetail::resolveImportForType).collect(Collectors.toSet());
  }

  public static List<String> resolveFieldsPaths(final String variableName,
                                                final CodeGenerationParameter aggregate,
                                                final List<CodeGenerationParameter> valueObjects) {
    return resolveFieldsPaths(variableName, aggregate.retrieveAllRelated(Label.STATE_FIELD), valueObjects);
  }

  public static List<String> resolveFieldsPaths(final String variableName,
                                                final Stream<CodeGenerationParameter> aggregateFields,
                                                final List<CodeGenerationParameter> valueObjects) {
    final List<String> paths = new ArrayList<>();
    aggregateFields.forEach(field -> resolveFieldPath(variableName, field, valueObjects, paths));
    return paths;
  }

  public static String resolveCompositeIdFieldsNames(final CodeGenerationParameter aggregate) {
    final List<String> compositeIdFields = aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> field.value).collect(toList());

    if(compositeIdFields.isEmpty())
      return "";

    return String.format("%s, ", String.join(", ", compositeIdFields));
  }

  public static String resolveCompositeIdFields(final CodeGenerationParameter aggregate) {
    final List<String> compositeIdFields = aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> String.format("%s %s", COMPOSITE_ID_DECLARATION_PATTERN, field.value)).collect(toList());

    if(compositeIdFields.isEmpty())
      return "";

    return String.format("%s, ", String.join(", ", compositeIdFields));
  }

  private static void resolveFieldPath(final String relativePath,
                                       final CodeGenerationParameter field,
                                       final List<CodeGenerationParameter> valueObjects,
                                       final List<String> paths) {
    final String currentRelativePath =
            relativePath.isEmpty() ? field.value : relativePath + "." + field.value;

    if (ValueObjectDetail.isValueObject(field)) {
      final String valueObjectType =
              field.retrieveRelatedValue(Label.FIELD_TYPE);

      final CodeGenerationParameter valueObject =
              ValueObjectDetail.valueObjectOf(valueObjectType, valueObjects.stream());

      valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
              .forEach(voField -> resolveFieldPath(currentRelativePath, voField, valueObjects, paths));
    } else {
      paths.add(currentRelativePath);
    }
  }

  public static Stream<CodeGenerationParameter> findInvolvedStateFields(final CodeGenerationParameter aggregate, final String methodName) {
    return findInvolvedStateFields(aggregate, methodName, (methodParameter, stateField) -> stateField);
  }

  public static <T> Stream<T> findInvolvedStateFields(final CodeGenerationParameter aggregate, final String methodName,
                                                      final BiFunction<CodeGenerationParameter, CodeGenerationParameter, T> converter) {
    final CodeGenerationParameter method = methodWithName(aggregate, methodName);
    final Stream<CodeGenerationParameter> methodParameters = method.retrieveAllRelated(Label.METHOD_PARAMETER);
    return methodParameters.map(parameter -> converter.apply(parameter, stateFieldWithName(aggregate, parameter.value)));
  }

  private static Optional<CodeGenerationParameter> findMethod(final CodeGenerationParameter aggregate, final String methodName) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .filter(method -> methodName.equals(method.value) || method.value.startsWith(methodName + "("))
            .findFirst();
  }

  public static boolean hasFactoryMethod(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .anyMatch(method -> method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf));
  }

  public static boolean hasMethodWithName(final CodeGenerationParameter aggregate, final String methodName) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).anyMatch(method -> methodName.equals(method.value));
  }

  public static List<String> retrieveSelfDescribingEvents(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT)
        .filter(domainEvent -> domainEvent.retrieveAllRelated(Label.STATE_FIELD).count() == 1)
        .map(domainEvent -> domainEvent.value)
        .collect(Collectors.toList());
  }

  public static Object resolveRootURI(final CodeGenerationParameter aggregate) {
     return aggregate.retrieveRelatedValue(Label.URI_ROOT).replace("{", "\" + ")
          .replace("}", " + \"");
    }
}
