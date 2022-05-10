// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AggregateDetail {

  public static String resolvePackage(final String basePackage, final String aggregateProtocolName) {
    return String.format("%s.%s.%s", basePackage, "Model", aggregateProtocolName);
  }

  public static CodeGenerationParameter stateFieldWithName(final CodeGenerationParameter aggregate, final String fieldName) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).filter(field -> field.value.equals(fieldName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found"));
  }

  public static CodeGenerationParameter methodWithName(final CodeGenerationParameter aggregate, final String methodName) {
    return findMethod(aggregate, methodName).orElseThrow(() -> new IllegalArgumentException("Method " + methodName + " not found"));
  }

  public static List<String> resolveFieldsPaths(final String variableName, final CodeGenerationParameter aggregate) {
    return resolveFieldsPaths(variableName, aggregate.retrieveAllRelated(Label.STATE_FIELD));
  }

  public static List<String> resolveFieldsPaths(final String variableName, final Stream<CodeGenerationParameter> aggregateFields) {
    final List<String> paths = new ArrayList<>();
    aggregateFields.forEach(field -> resolveFieldPath(variableName, field, paths));
    return paths;
  }

  public static String stateFieldType(final CodeGenerationParameter aggregate, final String fieldPath) {
    return stateFieldAtPath(1, aggregate, fieldPath.split("\\."));
  }

  private static String stateFieldAtPath(final int pathIndex, final CodeGenerationParameter parent, final String[] fieldPathParts) {
    final String fieldName = fieldPathParts[pathIndex];
    final CodeGenerationParameter field = stateFieldWithName(parent, fieldName);

    return field.hasAny(Label.COLLECTION_TYPE) ? FieldDetail.typeOf(parent, field.value) : field.retrieveRelatedValue(Label.FIELD_TYPE);
  }

  public static Set<String> resolveImports(final CodeGenerationParameter aggregate) {
    return resolveImports(aggregate.retrieveAllRelated(Label.STATE_FIELD));
  }

  public static Set<String> resolveImports(final Stream<CodeGenerationParameter> stateFields) {
    return stateFields.map(FieldDetail::resolveImportForType).collect(Collectors.toSet());
  }
  private static void resolveFieldPath(final String relativePath, final CodeGenerationParameter field, final List<String> paths) {
    final String currentRelativePath = relativePath.isEmpty() ? field.value : relativePath + "." + field.value;
    paths.add(currentRelativePath);
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
}
