// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.TestDataValueGenerator.TestDataValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.AGGREGATE_METHOD;

public class TestCase {

  private final String methodName;
  private final String resultAssignmentStatement;
  private final List<String> dataDeclarations = new ArrayList<>();
  private final List<String> preliminaryStatements = new ArrayList<>();
  private final List<String> assertions = new ArrayList<>();

  public static List<TestCase> from(final CodeGenerationParameter aggregate,
                                    final List<CodeGenerationParameter> valueObjects,
                                    final Optional<String> defaultFactoryMethod,
                                    final TestDataValues initialTestDataValues,
                                    final ProjectionType projectionType) {
    return aggregate.retrieveAllRelated(AGGREGATE_METHOD)
            .map(method -> new TestCase(method, aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues, projectionType))
            .collect(Collectors.toList());
  }

  private TestCase(final CodeGenerationParameter method,
                   final CodeGenerationParameter aggregate,
                   final List<CodeGenerationParameter> valueObjects,
                   final Optional<String> defaultFactoryMethod,
                   final TestDataValues initialTestDataValues,
                   final ProjectionType projectionType) {
    final TestDataValues updatedTestDataValues =
            initialTestDataValues.updateAllValues();

    final List<String> dataDeclarations =
            StaticDataDeclaration.generate(method, aggregate,
                    valueObjects, initialTestDataValues, updatedTestDataValues);

    final List<String> preliminaryStatements =
            PreliminaryStatement.resolve(method, defaultFactoryMethod);

    final String resultAssignmentStatement =
            ResultAssignmentStatement.resolve(aggregate, method);

    final List<String> assertions =
            Assertions.from(method, aggregate, valueObjects, defaultFactoryMethod,
                    initialTestDataValues, updatedTestDataValues, projectionType);

    this.methodName = method.value;
    this.resultAssignmentStatement = resultAssignmentStatement;
    this.preliminaryStatements.addAll(preliminaryStatements);
    this.dataDeclarations.addAll(dataDeclarations);
    this.assertions.addAll(assertions);
  }

  public String getMethodName() {
    return methodName;
  }

  public List<String> getDataDeclarations() {
    return dataDeclarations;
  }

  public String getResultAssignmentStatement() {
    return resultAssignmentStatement;
  }

  public List<String> getPreliminaryStatements() {
    return preliminaryStatements;
  }

  public List<String> getAssertions() {
    return assertions;
  }
}
