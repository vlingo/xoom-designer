// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.projections;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TestStatement {

  public final List<String> assertions = new ArrayList<>();
  public final List<String> secondAssertions = new ArrayList<>();
  public final List<String> resultAssignment = new ArrayList<>();

  public static List<TestStatement> with(final String testMethodName,
                                         CodeGenerationParameter aggregate, final CodeGenerationParameter domainEvent,
                                         final List<CodeGenerationParameter> valueObjects,
                                         final TestDataValueGenerator.TestDataValues testDataValues) {

    return Collections.singletonList(new TestStatement(1, testMethodName, aggregate, domainEvent,
        valueObjects, testDataValues));
  }

  private TestStatement(final int dataIndex,
                        final String testMethodName,
                        CodeGenerationParameter aggregate, final CodeGenerationParameter domainEvent,
                        final List<CodeGenerationParameter> valueObjects,
                        final TestDataValueGenerator.TestDataValues testDataValues) {
    this.resultAssignment.addAll(generateExecutions(dataIndex, testMethodName, aggregate));
    this.assertions.addAll(generateAssertions(dataIndex, aggregate, domainEvent, valueObjects, testDataValues));
    this.secondAssertions.addAll(generateAssertions(2, aggregate, domainEvent, valueObjects, testDataValues));
  }

  private List<String> generateExecutions(final int dataIndex,
                                          final String testMethodName,
                                          final CodeGenerationParameter aggregate) {
    final TestResultAssignment formatter =
        TestResultAssignment.forMethod(testMethodName);

    return Stream.of(formatter.formatMainResult(dataIndex, aggregate.value),
        formatter.formatFilteredResult(dataIndex, aggregate.value))
        .filter(assignment -> !assignment.isEmpty()).collect(toList());
  }

  private List<String> generateAssertions(final int dataIndex,
                                          final CodeGenerationParameter aggregate,final CodeGenerationParameter domainEvent,
                                          final List<CodeGenerationParameter> valueObjects,
                                          final TestDataValueGenerator.TestDataValues testDataValues) {
    return Assertions.from(dataIndex, aggregate, domainEvent, valueObjects, testDataValues);
  }


}
