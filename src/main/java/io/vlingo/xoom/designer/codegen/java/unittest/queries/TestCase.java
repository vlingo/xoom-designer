// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

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

  public static List<TestCase> from(final CodeGenerationParameter aggregate,
                                    final List<CodeGenerationParameter> valueObjects) {
    return Stream.of(TestCaseName.values()).map(name -> new TestCase(name.method, aggregate, valueObjects))
            .collect(Collectors.toList());
  }

  private TestCase(final String testMethodName,
                   final CodeGenerationParameter aggregate,
                   final List<CodeGenerationParameter> valueObjects) {
    final TestDataValueGenerator.TestDataValues testDataValues =
            TestDataValueGenerator.with(TEST_DATA_SET_SIZE, "data", aggregate, valueObjects).generate();

    this.methodName = testMethodName;
    this.dataDeclarations.addAll(StaticDataDeclaration.generate(testMethodName, aggregate, valueObjects, testDataValues));
    this.preliminaryStatements.addAll(PreliminaryStatement.with(testMethodName));
    this.statements.addAll(TestStatement.with(testMethodName, aggregate, valueObjects, testDataValues));
  }

  public static List<String> compositeIdFieldsFrom(CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> field.value)
        .collect(Collectors.toList());
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