// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCase {

  public final String methodName;
  public final String resultAssignmentStatement;
  public final List<String> dataDeclarations = new ArrayList<>();
  public final List<String> preliminaryStatements = new ArrayList<>();
  public final List<String> assertions = new ArrayList<>();
  public final boolean disabled;

  public static List<TestCase> from(final CodeGenerationParameter aggregate,
                                    final List<CodeGenerationParameter> valueObjects,
                                    final Optional<String> defaultFactoryMethod,
                                    final TestDataValueGenerator.TestDataValues initialTestDataValues) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .map(method -> new TestCase(method, aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues))
        .collect(Collectors.toList());
  }

  private TestCase(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                   final List<CodeGenerationParameter> valueObjects,
                   final Optional<String> defaultFactoryMethod, final TestDataValueGenerator.TestDataValues initialTestDataValues) {
    this.methodName = AggregateDetail.methodNameFrom(method);
    final TestDataValueGenerator.TestDataValues updatedTestDataValues = initialTestDataValues.updateAllValues();

    final List<String> dataDeclarations = StaticDataDeclaration.generate(method, aggregate, valueObjects, initialTestDataValues, updatedTestDataValues);

    final List<String> preliminaryStatements = PreliminaryStatement.resolve(method, defaultFactoryMethod);

    final String resultAssignmentStatement = ResultAssignmentStatement.resolve(aggregate, method);

    final List<String> assertions = Assertions.from(method, aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues, updatedTestDataValues);

    this.resultAssignmentStatement = resultAssignmentStatement;
    this.preliminaryStatements.addAll(preliminaryStatements);
    this.dataDeclarations.addAll(dataDeclarations);
    this.assertions.addAll(assertions);
    this.disabled = false;
  }

  public Set<String> involvedSpecialTypes() {
    return Stream.concat(CodeGenerationProperties.DATE_TIME_TYPES.stream(), CodeGenerationProperties.COLLECTION_TYPES.stream())
        .filter(specialType -> this.dataDeclarations.stream().anyMatch(declaration -> declaration.contains(specialType)))
        .collect(Collectors.toSet());
  }
}
