// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.DomainEventDetail;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class TestCase {

  public static final int TEST_DATA_SET_SIZE = 2;

  public final String methodName;
  public final List<String> dataDeclarations = new ArrayList<>();
  public final List<TestStatement> statements = new ArrayList<>();
  public final List<String> preliminaryStatements = new ArrayList<>();
  public final String domainEventName;
  public final String dataObjectParams;
  public final boolean factoryMethod;

  public static List<TestCase> from(final CodeGenerationParameter aggregate,
                                    final List<CodeGenerationParameter> valueObjects,
                                    final ProjectionType projectionType) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .filter(method -> method.retrieveOneRelated(Label.DOMAIN_EVENT).value != null)
        .map(method -> new TestCase(method, aggregate, valueObjects, projectionType))
        .collect(Collectors.toList());
  }

  private TestCase(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                   final List<CodeGenerationParameter> valueObjects, final ProjectionType projectionType) {
    final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
        .with(TEST_DATA_SET_SIZE, "data", aggregate, valueObjects).generate();

    final String domainEventName = method.retrieveOneRelated(Label.DOMAIN_EVENT).value;

    final CodeGenerationParameter domainEvent = DomainEventDetail
        .eventWithName(domainEventName, aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).collect(Collectors.toList()));

    this.methodName = AggregateDetail.methodNameFrom(method);
    this.domainEventName = domainEventName;
    this.factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    this.dataDeclarations.addAll(DataDeclaration.generate(aggregate, valueObjects, testDataValues));
    this.preliminaryStatements.addAll(PreliminaryStatement.with(method.value));
    this.statements.addAll(TestStatement.with(method.value, aggregate, domainEvent, valueObjects, testDataValues));
    this.dataObjectParams = resolveTestDataObjectParams(method, domainEvent, projectionType);
  }

  public String resolveTestDataObjectParams(final CodeGenerationParameter method, final CodeGenerationParameter domainEvent,
                                            final ProjectionType projectionType) {
    final Stream<CodeGenerationParameter> involvedFields =
        projectionType.isEventBased() ? domainEvent.retrieveAllRelated(Label.STATE_FIELD) :
            method.retrieveAllRelated(Label.METHOD_PARAMETER);

    return involvedFields.map(field -> {
      final CollectionMutation collectionMutation =
          field.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);
      if (collectionMutation.isSingleParameterBased()) {
        return String.format("data.%s.FirstOrDefault()", toPascalCase(field.value));
      }
      return "data." + toPascalCase(field.value);
    }).collect(Collectors.joining(", "));
  }

}
