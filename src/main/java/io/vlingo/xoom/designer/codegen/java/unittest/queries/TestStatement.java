// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.unittest.queries.TestCase.TEST_DATA_SET_SIZE;
import static java.util.stream.Collectors.toList;

public class TestStatement {

  private final List<String> assertions = new ArrayList<>();

  private final List<String> resultAssignment = new ArrayList<>();

  public static List<TestStatement> with(final String testMethodName,
                                         final CodeGenerationParameter aggregate,
                                         final List<CodeGenerationParameter> valueObjects,
                                         final TestDataValueGenerator.TestDataValues testDataValues) {
    final IntFunction<TestStatement> mapper =
            dataIndex -> new TestStatement(dataIndex, testMethodName, aggregate,
                    valueObjects, testDataValues);

    return IntStream.range(1, TEST_DATA_SET_SIZE + 1).mapToObj(mapper).collect(toList());
  }

  private TestStatement(final int dataIndex,
                        final String testMethodName,
                        final CodeGenerationParameter aggregate,
                        final List<CodeGenerationParameter> valueObjects,
                        final TestDataValueGenerator.TestDataValues testDataValues) {
    final List<String> compositeIdFields = TestCase.compositeIdFieldsFrom(aggregate);
    if(compositeIdFields.isEmpty())
      this.resultAssignment.addAll(generateExecutions(dataIndex, testMethodName, aggregate));
    else
      this.resultAssignment.addAll(generateExecutionsWithCompositeId(dataIndex, testMethodName, aggregate, compositeIdFields));
    this.assertions.addAll(generateAssertions(dataIndex, aggregate, valueObjects, testDataValues));
  }

  private Collection<String> generateExecutionsWithCompositeId(int dataIndex, String testMethodName, CodeGenerationParameter aggregate, List<String> compositeIdFields) {
    final CompositeIdTestResultAssignment formatter =
        CompositeIdTestResultAssignment.forMethod(testMethodName);

    final String compositeIdConcatenation = compositeIdFields
        .stream().map(id -> "\"" + dataIndex + "\"").collect(Collectors.joining(","));

    return Stream.of(formatter.formatMainResult(dataIndex, compositeIdConcatenation, aggregate.value),
            formatter.formatFilteredResult(dataIndex, compositeIdConcatenation, aggregate.value))
        .filter(assignment -> !assignment.isEmpty()).collect(toList());
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
                                          final CodeGenerationParameter aggregate,
                                          final List<CodeGenerationParameter> valueObjects,
                                          final TestDataValueGenerator.TestDataValues testDataValues) {
    return Assertions.from(dataIndex, aggregate, valueObjects, testDataValues);
  }

  public List<String> getAssertions() {
    return assertions;
  }

  public List<String> getResultAssignment() {
    return resultAssignment;
  }

}
