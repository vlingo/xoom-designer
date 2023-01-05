// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TestCase {

  public static final int TEST_DATA_SET_SIZE = 2;

  private final String methodName;
  private final String dataDeclaration;
  private final List<TestStatement> statements = new ArrayList<>();
  private final List<String> preliminaryStatements = new ArrayList<>();
  private final String rootMethod;
  private final List<String> selfDescribingEvents;
  private boolean isMethodEmitSelfDescribingEvent;
  private boolean isMethodParametersMapDomainEventFields;

  public static List<TestCase> from(final CodeGenerationParameter aggregate,
      final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)
        .map(signature -> new TestCase(signature, aggregate, valueObjects))
        .collect(Collectors.toList());
  }

  private TestCase(final CodeGenerationParameter signature, final CodeGenerationParameter aggregate,
      final List<CodeGenerationParameter> valueObjects) {
    final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
        .with(TEST_DATA_SET_SIZE, "data", aggregate, valueObjects).generate();


    final String dataObjectType = JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);
    this.methodName = signature.value;

    this.selfDescribingEvents = AggregateDetail.retrieveSelfDescribingEvents(aggregate);

    if(AggregateDetail.hasMethodWithName(aggregate, signature.value)) {
      this.isMethodParametersMapDomainEventFields = isMethodParametersMapDomainEventFields(AggregateDetail.methodWithName(aggregate, signature.value), aggregate);
      this.isMethodEmitSelfDescribingEvent = isMethodEmitSelfDescribingEvent(AggregateDetail.methodWithName(aggregate, signature.value));
    }

    this.dataDeclaration = DataDeclaration.generate(signature.value, aggregate, valueObjects, testDataValues);
    this.rootMethod = signature.retrieveRelatedValue(Label.ROUTE_METHOD).toLowerCase(Locale.ROOT);
    this.preliminaryStatements.addAll(PreliminaryStatement.with(aggregate.retrieveRelatedValue(Label.URI_ROOT),
        dataObjectType, rootPath(signature, aggregate), rootMethod));
    this.statements.addAll(TestStatement.with(rootPath(signature, aggregate), rootMethod, aggregate, valueObjects, testDataValues));
  }

  private boolean isMethodEmitSelfDescribingEvent(final CodeGenerationParameter method) {
    return selfDescribingEvents.contains(method.retrieveRelatedValue(Label.DOMAIN_EVENT));
  }

  private boolean isMethodParametersMapDomainEventFields(final CodeGenerationParameter method,
                                                         final CodeGenerationParameter aggregate) {
    final List<CodeGenerationParameter> involvedStateFieldTypes = method
        .retrieveAllRelated(Label.METHOD_PARAMETER)
        .map(parameter -> AggregateDetail.stateFieldWithName(aggregate, parameter.value))
        .collect(toList());
    final List<CodeGenerationParameter> involvedEventStateFieldTypes = method.retrieveOneRelated(Label.DOMAIN_EVENT)
        .retrieveAllRelated(Label.STATE_FIELD)
        .map(parameter -> AggregateDetail.stateFieldWithName(aggregate, parameter.value))
        .filter(param -> !param.value.equals("id"))
        .collect(toList());
    return new HashSet<>(involvedStateFieldTypes).containsAll(involvedEventStateFieldTypes);
  }

  private String rootPath(final CodeGenerationParameter signature, final CodeGenerationParameter aggregate) {
    String uriRoot = aggregate.retrieveRelatedValue(Label.URI_ROOT);
    return signature.retrieveRelatedValue(Label.ROUTE_PATH).startsWith(uriRoot)
        ? signature.retrieveRelatedValue(Label.ROUTE_PATH)
        : uriRoot + signature.retrieveRelatedValue(Label.ROUTE_PATH);
  }
  
  public String getMethodName() {
    return methodName;
  }

  public String getRootMethod() {
    return rootMethod;
  }

  public boolean isRootMethod() {
    return !getRootMethod().equals("post");
  }

  public boolean isDisabled() {
    return !isMethodParametersMapDomainEventFields;
  }

  public boolean isMethodEmitSelfDescribingEvent() {
    return isMethodEmitSelfDescribingEvent;
  }

  public String selfDescribingEvents() {
    return selfDescribingEvents.isEmpty() ? "" : Arrays.toString(selfDescribingEvents.toArray());
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
