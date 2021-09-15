// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.resource;


import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;

import java.util.function.BiFunction;

class TestResultAssignment {

  private static final TestResultAssignment SAVE_TEST_CASE_EXECUTION =
      new TestResultAssignment(TestResultAssignment::resolveSaveResultAssignment);

  private final BiFunction<Integer, String, String> mainResultFormatting;
  private final BiFunction<Integer, String, String> filteredResultFormatting;

  public static TestResultAssignment forMethod(final String testCaseMethod) {
    return SAVE_TEST_CASE_EXECUTION;
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

  private static String resolveSaveResultAssignment(final int dataIndex, final String aggregateName) {
    @SuppressWarnings("unused")
    final String dataObjectName =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);

    return "";
  }
}
