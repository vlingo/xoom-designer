// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Assertions {

  public static List<String> from(final CodeGenerationParameter method,
                                  final CodeGenerationParameter aggregate,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final Optional<String> defaultFactoryMethod,
                                  final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                  final TestDataValueGenerator.TestDataValues updatedTestDataValues,
                                  final ProjectionType projectionType) {

    final List<String> entityFieldAssertions =
            AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ?
                    mergeEntityFieldAssertions(defaultFactoryMethod.get(), method, aggregate,
                            valueObjects, initialTestDataValues, updatedTestDataValues) :
                    resolveEntityFieldAssertions(method, aggregate, valueObjects, initialTestDataValues);

    final List<String> dispatcherAssertions =
            resolveDispatcherAssertions(method, defaultFactoryMethod, projectionType);

    return Stream.of(entityFieldAssertions, dispatcherAssertions).flatMap(List::stream).collect(Collectors.toList());
  }

  private static List<String> resolveDispatcherAssertions(final CodeGenerationParameter method,
                                                          final Optional<String> defaultFactoryMethod,
                                                          final ProjectionType projectionType) {
    final String eventName = method.retrieveRelatedValue(Label.DOMAIN_EVENT);
    final String stateName = JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(method.parent(Label.AGGREGATE).value);
    final int expectedNumberOfEntries = AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethod) ? 2 : 1;
    final int entryIndex = expectedNumberOfEntries - 1;

    if (projectionType.isOperationBased()) {
      return Arrays.asList(
              String.format("assertEquals(%s, (int) dispatcherAccess.readFrom(\"storeCount\"));", expectedNumberOfEntries),
              String.format("assertEquals(%s.class.getName(), dispatcherAccess.readFrom(\"storedAt\", %s));", stateName, entryIndex)
      );
    }

    if(eventName != null && !eventName.isEmpty()) {
      return Arrays.asList(
              String.format("assertEquals(%s, (int) dispatcherAccess.readFrom(\"entriesCount\"));", expectedNumberOfEntries),
              String.format("assertEquals(%s.class.getName(), ((BaseEntry<String>) dispatcherAccess.readFrom(\"appendedAt\", %s)).typeName());", eventName, entryIndex)
      );
    }

    return Collections.emptyList();
  }

  private static List<String> resolveEntityFieldAssertions(final CodeGenerationParameter method,
                                                           final CodeGenerationParameter aggregate,
                                                           final List<CodeGenerationParameter> valueObjects,
                                                           final TestDataValueGenerator.TestDataValues testDataValues) {
    final Stream<CodeGenerationParameter> stateFields =
            AggregateDetail.findInvolvedStateFields(aggregate, method.value);

    return formatAssertions(aggregate, stateFields, valueObjects, testDataValues);
  }

  private static List<String> mergeEntityFieldAssertions(final String defaultFactoryMethod,
                                                         final CodeGenerationParameter updateMethod,
                                                         final CodeGenerationParameter aggregate,
                                                         final List<CodeGenerationParameter> valueObjects,
                                                         final TestDataValueGenerator.TestDataValues initialTestDataValues,
                                                         final TestDataValueGenerator.TestDataValues updatedTestDataValues) {
    if(!updateMethod.hasAny(Label.METHOD_PARAMETER)) {
      return Collections.emptyList();
    }

    final Stream<CodeGenerationParameter> factoryMethodFields =
            AggregateDetail.findInvolvedStateFields(aggregate, defaultFactoryMethod);

    final List<CodeGenerationParameter> updateMethodFields =
            AggregateDetail.findInvolvedStateFields(aggregate, updateMethod.value).collect(Collectors.toList());

    final Stream<CodeGenerationParameter> exclusiveFactoryMethodFields =
            filterExclusiveFactoryMethodFields(factoryMethodFields, updateMethodFields);

    final List<String> factoryMethodAssertions =
            formatAssertions(aggregate, exclusiveFactoryMethodFields, valueObjects, initialTestDataValues);

    final List<String> updateMethodAssertions =
            formatAssertions(aggregate, updateMethodFields.stream(), valueObjects, updatedTestDataValues);

    return Stream.of(factoryMethodAssertions, updateMethodAssertions)
            .flatMap(List::stream).collect(Collectors.toList());
  }

  private static List<String> formatAssertions(final CodeGenerationParameter aggregate,
                                               final Stream<CodeGenerationParameter> stateFields,
                                               final List<CodeGenerationParameter> valueObjects,
                                               final TestDataValueGenerator.TestDataValues testDataValues) {
    final List<String> fieldPaths =
            AggregateDetail.resolveFieldsPaths("state", stateFields, valueObjects);

    final Function<String, String> mapper =
            fieldPath -> {
              final String fieldType = AggregateDetail.stateFieldType(aggregate, fieldPath, valueObjects);
              if(FieldDetail.isCollection(fieldType) || FieldDetail.isDateTime(fieldType)) {
                return String.format("assertNotNull(%s);", fieldPath);
              }
              return String.format("assertEquals(%s, %s);", fieldPath, testDataValues.retrieve("state", fieldPath));
            };

    return fieldPaths.stream().map(mapper).collect(Collectors.toList());
  }

  private static Stream<CodeGenerationParameter> filterExclusiveFactoryMethodFields(final Stream<CodeGenerationParameter> factoryMethodFields,
                                                                                    final List<CodeGenerationParameter> updateMethodFields) {
    final Set<String> updateMethodFieldNames =
            updateMethodFields.stream().map(field -> field.value).collect(Collectors.toSet());

    return factoryMethodFields.filter(field -> !updateMethodFieldNames.contains(field.value));
  }

}
