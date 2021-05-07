// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.queries;


import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.QueriesDetail;

import java.util.function.BiFunction;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;

class TestResultAssignment {

  private static final TestResultAssignment QUERY_BY_ID_TEST_CASE_EXECUTION =
          new TestResultAssignment(TestResultAssignment::resolveQueryByIdResultAssignment);

  private static final TestResultAssignment QUERY_ALL_TEST_CASE_EXECUTION =
          new TestResultAssignment(TestResultAssignment::resolveQueryAllResultAssignment,
                  TestResultAssignment::resolveQueryAllFilteredResultAssignment);

  private final BiFunction<Integer, String, String> mainResultFormatting;
  private final BiFunction<Integer, String, String> filteredResultFormatting;

  public static TestResultAssignment forMethod(final String testCaseMethod) {
    if (TestCaseName.ofMethod(testCaseMethod).equals(TestCaseName.QUERY_BY_ID)) {
      return QUERY_BY_ID_TEST_CASE_EXECUTION;
    }
    return QUERY_ALL_TEST_CASE_EXECUTION;
  }

  private TestResultAssignment(final BiFunction<Integer, String, String> resultAssignmentPattern) {
    this(resultAssignmentPattern, (d, a) -> "");
  }

  private TestResultAssignment(final BiFunction<Integer, String, String> resultAssignmentResolver,
                               final BiFunction<Integer, String, String> filteredResultAssignmentPattern) {
    this.mainResultFormatting = resultAssignmentResolver;
    this.filteredResultFormatting = filteredResultAssignmentPattern;
  }

  public String formatMainResult(final int dataIndex, final String aggregateName) {
    return mainResultFormatting.apply(dataIndex, aggregateName);
  }

  public String formatFilteredResult(final int dataIndex, final String aggregateName) {
    return filteredResultFormatting.apply(dataIndex, aggregateName);
  }

  private static String resolveQueryByIdResultAssignment(final int dataIndex, final String aggregateName) {
    final String dataObjectName =
            DATA_OBJECT.resolveClassname(aggregateName);

    final String queryMethodName =
            QueriesDetail.resolveQueryByIdMethodName(aggregateName);

    final String variableName =
            TestDataFormatter.formatLocalVariableName(dataIndex);

    return String.format("final %s %s = queries.%s(\"%s\").await();",
            dataObjectName, variableName, queryMethodName, dataIndex);
  }

  private static String resolveQueryAllResultAssignment(final int dataIndex, final String aggregateName) {
    if (dataIndex > 1) {
      return "";
    }

    final String dataObjectName =
            DATA_OBJECT.resolveClassname(aggregateName);

    final String queryMethodName =
            QueriesDetail.resolveQueryAllMethodName(aggregateName);

    return String.format("final Collection<%s> results = queries.%s().await();",
            dataObjectName, queryMethodName);
  }

  private static String resolveQueryAllFilteredResultAssignment(final int dataIndex, final String aggregateName) {
    final String dataObjectName =
            DATA_OBJECT.resolveClassname(aggregateName);

    final String variableName =
            TestDataFormatter.formatLocalVariableName(dataIndex);

    return String.format("final %s %s = results.stream().filter(data -> data.id.equals(\"%s\")).findFirst().orElseThrow(RuntimeException::new);",
            dataObjectName, variableName, dataIndex);
  }
}
