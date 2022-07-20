// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Assertions {

  public static List<String> from(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                  final Optional<String> defaultFactoryMethod,
                                  final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                  final TestDataValueGenerator.TestDataValues updatedTestDataValues) {

    final List<String> entityFieldAssertions = AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ?
        mergeEntityFieldAssertions(defaultFactoryMethod.get(), method, aggregate, initialTestDataValues, updatedTestDataValues) :
        resolveEntityFieldAssertions(method, aggregate, initialTestDataValues);

    final List<String> dispatcherAssertions = resolveDispatcherAssertions(method, defaultFactoryMethod);

    return Stream.of(entityFieldAssertions, dispatcherAssertions).flatMap(List::stream).collect(Collectors.toList());
  }

  private static List<String> resolveDispatcherAssertions(final CodeGenerationParameter method,
                                                          final Optional<String> defaultFactoryMethod) {
    final String eventName = method.retrieveRelatedValue(Label.DOMAIN_EVENT);
    final int expectedNumberOfStates = AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ? 2 : 1;

    if (eventName != null && !eventName.isEmpty()) {
      return Arrays.asList(
          String.format("Assert.Equal(%s, dispatcherAccess.ReadFrom<int>(\"statesCount\"));", expectedNumberOfStates),
          "Assert.Equal(state.GetType(), dispatcherAccess.ReadFrom<int, IState>(\"appendedStateAt\", 0).Typed);"
      );
    }

    return Collections.emptyList();
  }

  private static List<String> resolveEntityFieldAssertions(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                                           final TestDataValueGenerator.TestDataValues testDataValues) {
    final Stream<CodeGenerationParameter> stateFields = AggregateDetail.findInvolvedStateFields(aggregate, method.value);
    return formatAssertions(aggregate, stateFields, testDataValues);
  }

  private static List<String> mergeEntityFieldAssertions(final String defaultFactoryMethod, final CodeGenerationParameter updateMethod,
                                                         final CodeGenerationParameter aggregate,
                                                         final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                                         final TestDataValueGenerator.TestDataValues updatedTestDataValues) {
    if (!updateMethod.hasAny(Label.METHOD_PARAMETER)) {
      return Collections.emptyList();
    }

    final Stream<CodeGenerationParameter> factoryMethodFields = AggregateDetail
        .findInvolvedStateFields(aggregate, defaultFactoryMethod);

    final List<CodeGenerationParameter> updateMethodFields = AggregateDetail
        .findInvolvedStateFields(aggregate, updateMethod.value).collect(Collectors.toList());

    final Stream<CodeGenerationParameter> exclusiveFactoryMethodFields = filterExclusiveFactoryMethodFields(factoryMethodFields, updateMethodFields);

    final List<String> factoryMethodAssertions = formatAssertions(aggregate, exclusiveFactoryMethodFields, initialTestDataValues);

    final List<String> updateMethodAssertions = formatAssertions(aggregate, updateMethodFields.stream(), updatedTestDataValues);

    return Stream.of(factoryMethodAssertions, updateMethodAssertions)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private static List<String> formatAssertions(final CodeGenerationParameter aggregate, final Stream<CodeGenerationParameter> stateFields,
                                               final TestDataValueGenerator.TestDataValues testDataValues) {
    final List<String> fieldPaths = AggregateDetail.resolveFieldsPaths("state", stateFields);
    final Function<String, String> mapper = fieldPath -> assertionByStateFieldType(aggregate, testDataValues, fieldPath);

    return fieldPaths.stream().map(mapper).collect(Collectors.toList());
  }

  private static String assertionByStateFieldType(final CodeGenerationParameter aggregate,
                                                  final TestDataValueGenerator.TestDataValues testDataValues,
                                                  final String fieldPath) {
    final String fieldType = AggregateDetail.stateFieldType(aggregate, fieldPath);
    if (FieldDetail.isCollection(fieldType) || FieldDetail.isDateTime(fieldType)) {
      return String.format("Assert.NotNull(%s);", fieldPath);
    }
    return String.format("Assert.Equal(%s, %s);",  testDataValues.retrieve("state", fieldPath), fieldPath);
  }

  private static Stream<CodeGenerationParameter> filterExclusiveFactoryMethodFields(final Stream<CodeGenerationParameter> factoryMethodFields,
                                                                                    final List<CodeGenerationParameter> updateMethodFields) {
    final Set<String> updateMethodFieldNames = updateMethodFields.stream()
        .map(field -> field.value)
        .collect(Collectors.toSet());

    return factoryMethodFields.filter(field -> !updateMethodFieldNames.contains(field.value));
  }

}
