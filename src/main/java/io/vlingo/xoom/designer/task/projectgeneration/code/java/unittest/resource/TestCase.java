// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.TestDataValueGenerator;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.queries.PreliminaryStatement;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.queries.StaticDataDeclaration;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.queries.TestCaseName;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.queries.TestStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCase {

  public static final int TEST_DATA_SET_SIZE = 2;

  private final String methodName;
  private final List<String> dataDeclarations = new ArrayList<>();
  private final List<TestStatement> statements = new ArrayList<>();
  private final List<String> preliminaryStatements = new ArrayList<>();

  public static List<TestCase> from(final CodeGenerationParameter aggregate) {
    return Stream.of(TestCaseName.values()).map(name -> new TestCase(name.method, aggregate))
            .collect(Collectors.toList());
  }

  private TestCase(final String testMethodName,
                   final CodeGenerationParameter aggregate) {
    final TestDataValueGenerator.TestDataValues testDataValues =
            TestDataValueGenerator.with(TEST_DATA_SET_SIZE, "data", aggregate, new ArrayList<>()).generate();

    this.methodName = testMethodName;
    this.dataDeclarations.addAll(StaticDataDeclaration.generate(testMethodName, aggregate, new ArrayList<>(), testDataValues));
    this.preliminaryStatements.addAll(PreliminaryStatement.with(testMethodName));
    this.statements.addAll(TestStatement.with(testMethodName, aggregate, new ArrayList<>(), testDataValues));
  }

  public String getMethodName() {
    return methodName;
  }

  public List<String> getDataDeclarations() {
    return dataDeclarations;
  }

  public List<TestStatement> getStatements() {
    return statements;
  }

  public List<String> getPreliminaryStatements() {
    return preliminaryStatements;
  }


}