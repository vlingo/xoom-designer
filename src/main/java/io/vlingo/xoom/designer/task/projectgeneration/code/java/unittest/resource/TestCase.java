// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TestCase {

  public static final int TEST_DATA_SET_SIZE = 2;

  private final String methodName;
  private final String dataDeclaration;
  private final List<TestStatement> statements = new ArrayList<>();
  private final List<String> preliminaryStatements = new ArrayList<>();

  public static List<TestCase> from(final CodeGenerationParameter aggregate, List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)
        .map(signature -> new TestCase(signature, aggregate, valueObjects))
        .collect(Collectors.toList());
  }

  private TestCase(final CodeGenerationParameter signature, final CodeGenerationParameter aggregate,
                   List<CodeGenerationParameter> valueObjects) {
    final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
        .with(TEST_DATA_SET_SIZE, "data", aggregate, valueObjects).generate();

    final String dataObjectType =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);
    this.methodName = "test" + toCamelCase(signature.value);
    this.dataDeclaration = DataDeclaration.generate(signature.value, aggregate, valueObjects, testDataValues);
    this.preliminaryStatements.addAll(PreliminaryStatement.with(signature.value, dataObjectType,
        signature.retrieveRelatedValue(Label.ROUTE_PATH), signature.retrieveRelatedValue(Label.ROUTE_METHOD).toLowerCase(Locale.ROOT)));
    this.statements.addAll(TestStatement.with(signature.value, aggregate, valueObjects, testDataValues));
  }

  private String toCamelCase(String testMethodName) {
    return Character.toString(testMethodName.charAt(0)).toUpperCase() + testMethodName.substring(1);
  }

  public String getMethodName() {
    return methodName;
  }

  public String getDataDeclaration() {
    return dataDeclaration;
  }

  public List<TestStatement> getStatements() {
    return statements;
  }

  public List<String> getPreliminaryStatements() {
    return preliminaryStatements;
  }


}