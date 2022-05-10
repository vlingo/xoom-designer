// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.Label.METHOD_PARAMETER;

public class AuxiliaryEntityCreation {

  public static final String METHOD_NAME = "_createEntity";
  private final String methodName;
  private final String statement;
  private final String defaultFactoryMethodName;
  private final List<String> dataDeclarations = new ArrayList<>();
  private final boolean required;

  public static AuxiliaryEntityCreation from(final CodeGenerationParameter aggregate, final Optional<String> defaultFactoryMethod,
                                             final TestDataValueGenerator.TestDataValues testDataValues) {
    return defaultFactoryMethod
        .map(s -> new AuxiliaryEntityCreation(s, aggregate, testDataValues))
        .orElseGet(AuxiliaryEntityCreation::new);
  }

  public AuxiliaryEntityCreation(final String factoryMethodName, final CodeGenerationParameter aggregate,
                                 final TestDataValueGenerator.TestDataValues testDataValues) {
    final CodeGenerationParameter method = AggregateDetail.methodWithName(aggregate, factoryMethodName);

    this.methodName = METHOD_NAME;
    this.defaultFactoryMethodName = factoryMethodName;
    this.statement = resolveStatement(method, aggregate);
    this.dataDeclarations.addAll(resolveDataDeclarations(method, aggregate, testDataValues));
    this.required = true;
  }

  private AuxiliaryEntityCreation() {
    this.methodName = null;
    this.statement = null;
    this.defaultFactoryMethodName = null;
    this.required = false;
  }

  private String resolveStatement(final CodeGenerationParameter method, final CodeGenerationParameter aggregate) {
    final String entityMethodInvocation = ResultAssignmentStatement.resolveEntityMethodInvocation(aggregate, method);

    return fixStaticDataVariablesName(entityMethodInvocation);
  }

  private List<String> resolveDataDeclarations(final CodeGenerationParameter method, final CodeGenerationParameter aggregate,
                                               final TestDataValueGenerator.TestDataValues testDataValues) {
    final List<String> declarations = StaticDataDeclaration.generate(method, aggregate, testDataValues);

    return declarations.stream().map(this::fixStaticDataVariablesName).collect(Collectors.toList());
  }

  private String fixStaticDataVariablesName(final String statement) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final String constantMiddleName = codeElementFormatter.staticConstant(defaultFactoryMethodName) + "_TEST";
    return statement.replaceAll(constantMiddleName, "ENTITY_CREATION");
  }

  public String getStatement() {
    return statement;
  }

  public boolean isRequired() {
    return required;
  }

  public String getMethodName() {
    return methodName;
  }

  public List<String> getDataDeclarations() {
    return dataDeclarations;
  }

  public static boolean isRequiredFor(final CodeGenerationParameter method, final Optional<String> defaultFactoryMethod) {
    return !method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf) && method.hasAny(METHOD_PARAMETER) && defaultFactoryMethod.isPresent();
  }
}
