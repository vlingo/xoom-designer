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

  private static final String ASSERT_NOT_NULL_PLACEHOLDER = "Assert.NotNull(%s.%s);";
  private static final String ASSERT_EQUAL_PLACEHOLDER = "Assert.Equal(%s, %s.%s);";
  private static final String VARIABLE_NAME = "state";
  private static final String ASSERT_EQUAL_DISPATCHER_ACCESS_STATES_COUNT = "Assert.Equal(%s, dispatcherAccess.ReadFrom<int>(\"statesCount\"));";
  private static final String ASSERT_EQUAL_STATE_GET_TYPE_DISPATCHER_ACCESS_READ_STATE_TYPE = "Assert.Equal(state.GetType(), dispatcherAccess.ReadFrom<int, IState>(\"appendedStateAt\", 0).Typed);";

  public static List<String> from(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final Optional<String> defaultFactoryMethod,
                                  final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                  final TestDataValueGenerator.TestDataValues updatedTestDataValues) {

    final List<String> entityFieldAssertions = AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ?
        mergeEntityFieldAssertions(defaultFactoryMethod.get(), method, aggregate, valueObjects, initialTestDataValues, updatedTestDataValues) :
        resolveEntityFieldAssertions(method, aggregate, valueObjects, initialTestDataValues);

    final List<String> dispatcherAssertions = resolveDispatcherAssertions(method, defaultFactoryMethod);

    return Stream.of(entityFieldAssertions, dispatcherAssertions).flatMap(List::stream).collect(Collectors.toList());
  }

  private static List<String> resolveDispatcherAssertions(final CodeGenerationParameter method,
                                                          final Optional<String> defaultFactoryMethod) {
    final String eventName = method.retrieveRelatedValue(Label.DOMAIN_EVENT);
    final int expectedNumberOfStates = AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ? 2 : 1;

    if (!method.hasAny(Label.METHOD_PARAMETER) && eventName != null && !eventName.isEmpty())
      return Collections.singletonList(
          String.format(ASSERT_EQUAL_DISPATCHER_ACCESS_STATES_COUNT, expectedNumberOfStates)
      );

    if(eventName != null && !eventName.isEmpty()) {
      return Arrays.asList(
          String.format(ASSERT_EQUAL_DISPATCHER_ACCESS_STATES_COUNT, expectedNumberOfStates),
          ASSERT_EQUAL_STATE_GET_TYPE_DISPATCHER_ACCESS_READ_STATE_TYPE
      );
    }

    return Collections.emptyList();
  }

  private static List<String> resolveEntityFieldAssertions(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                                           final List<CodeGenerationParameter> valueObjects,
                                                           final TestDataValueGenerator.TestDataValues testDataValues) {
    final Stream<CodeGenerationParameter> stateFields = AggregateDetail.findInvolvedStateFields(aggregate, method.value);
    return formatAssertions(aggregate, valueObjects, stateFields, testDataValues);
  }

  private static List<String> mergeEntityFieldAssertions(final String defaultFactoryMethod, final CodeGenerationParameter updateMethod,
                                                         final CodeGenerationParameter aggregate,
                                                         final List<CodeGenerationParameter> valueObjects,
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

    final List<String> factoryMethodAssertions = formatAssertions(aggregate, valueObjects, exclusiveFactoryMethodFields, initialTestDataValues);

    final List<String> updateMethodAssertions = formatAssertions(aggregate, valueObjects, updateMethodFields.stream(), updatedTestDataValues);

    return Stream.of(factoryMethodAssertions, updateMethodAssertions)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private static List<String> formatAssertions(final CodeGenerationParameter aggregate,
                                               final List<CodeGenerationParameter> valueObjects,
                                               final Stream<CodeGenerationParameter> stateFields,
                                               final TestDataValueGenerator.TestDataValues testDataValues) {
    final List<String> fieldPaths = AggregateDetail.resolveFieldsPaths(VARIABLE_NAME, stateFields, valueObjects);
    final Function<String, String> mapper = fieldPath -> assertionByStateFieldType(aggregate, valueObjects, testDataValues, fieldPath);

    return fieldPaths.stream().map(mapper).collect(Collectors.toList());
  }

  private static String assertionByStateFieldType(final CodeGenerationParameter aggregate,
                                                  final List<CodeGenerationParameter> valueObjects,
                                                  final TestDataValueGenerator.TestDataValues testDataValues,
                                                  final String fieldPath) {
    final Function<String, String> toPascalCaseMapper = path -> Arrays.stream(path.split("\\."))
        .skip(1)
        .map(FieldDetail::toPascalCase).collect(Collectors.joining("."));
    final String fieldType = AggregateDetail.stateFieldType(aggregate, fieldPath, valueObjects);
    if (FieldDetail.isCollection(fieldType) || FieldDetail.isDateTime(fieldType)) {
      return String.format(ASSERT_NOT_NULL_PLACEHOLDER, VARIABLE_NAME, toPascalCaseMapper.apply(fieldPath));
    }
    return String.format(ASSERT_EQUAL_PLACEHOLDER, testDataValues.retrieve(VARIABLE_NAME, fieldPath), VARIABLE_NAME, toPascalCaseMapper.apply(fieldPath));
  }

  private static Stream<CodeGenerationParameter> filterExclusiveFactoryMethodFields(final Stream<CodeGenerationParameter> factoryMethodFields,
                                                                                    final List<CodeGenerationParameter> updateMethodFields) {
    final Set<String> updateMethodFieldNames = updateMethodFields.stream()
        .map(field -> field.value)
        .collect(Collectors.toSet());

    return factoryMethodFields.filter(field -> !updateMethodFieldNames.contains(field.value));
  }

}
