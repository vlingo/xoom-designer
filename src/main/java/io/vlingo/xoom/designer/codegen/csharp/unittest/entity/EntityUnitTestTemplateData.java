// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.*;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class EntityUnitTestTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage, final StorageType storageType,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final List<Content> contents) {
    return aggregates.stream()
        .map(aggregate -> new EntityUnitTestTemplateData(basePackage, storageType, aggregate, valueObjects, contents))
        .collect(Collectors.toList());
  }

  private EntityUnitTestTemplateData(final String basePackage, final StorageType storageType,
                                     final CodeGenerationParameter aggregate,
                                     final List<CodeGenerationParameter> valueObjects,
                                     final List<Content> contents) {
    final String entityName = CsharpTemplateStandard.AGGREGATE.resolveClassname(aggregate.value);

    final CodeElementFormatter formatter = ComponentRegistry.withName("cSharpCodeFormatter");

    final String stateName = CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value);

    final String packageName = ContentQuery
        .findPackage(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "I" + aggregate.value, contents)
        .replace(basePackage, basePackage + ".Tests");

    final TestDataValueGenerator.TestDataValues initialTestDataValues = TestDataValueGenerator.with(aggregate, valueObjects).generate();

    final Optional<String> defaultFactoryMethod = resolveDefaultFactoryMethodName(aggregate);

    final AuxiliaryEntityCreation auxiliaryEntityCreation = AuxiliaryEntityCreation.from(aggregate, valueObjects,
        defaultFactoryMethod, initialTestDataValues);

    final List<TestCase> testCases = TestCase.from(storageType, aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues);

    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.DISPATCHER_NAME, CsharpTemplateStandard.MOCK_DISPATCHER.resolveClassname())
        .and(TemplateParameter.AUXILIARY_ENTITY_CREATION, auxiliaryEntityCreation)
        .and(TemplateParameter.ENTITY_CREATION_METHOD, AuxiliaryEntityCreation.METHOD_NAME)
        .and(TemplateParameter.AGGREGATE_PROTOCOL_VARIABLE, formatter.simpleNameToAttribute(aggregate.value))
        .and(TemplateParameter.ENTITY_UNIT_TEST_NAME, CsharpTemplateStandard.ENTITY_UNIT_TEST.resolveClassname(entityName))
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, "I" + aggregate.value)
        .and(TemplateParameter.ENTITY_NAME, entityName)
        .and(TemplateParameter.STATE_NAME, stateName)
        .and(TemplateParameter.ADAPTER_NAME, CsharpTemplateStandard.ADAPTER.resolveClassname(stateName))
        .and(TemplateParameter.SOURCED_EVENTS, SourcedEvent.from(storageType, aggregate))
        .and(TemplateParameter.TEST_CASES, testCases)
        .and(TemplateParameter.STORAGE_TYPE, storageType)
        .and(TemplateParameter.PRODUCTION_CODE, false)
        .and(TemplateParameter.UNIT_TEST, true)
        .addImports(resolveImports(basePackage, aggregate, testCases, contents));
  }

  private Set<String> resolveMethodParameterImports(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .map(method -> AggregateDetail.findInvolvedStateFields(aggregate, method.value))
        .map(AggregateDetail::resolveImports)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  private String resolveMockDispatcherImport(final String basePackage) {
    return MockDispatcherDetail.resolvePackage(basePackage);
  }

  private String resolveValueObjectImports(final CodeGenerationParameter aggregate, final List<Content> contents) {
    if (!ValueObjectDetail.useValueObject(aggregate)) {
      return "";
    }
    return ContentQuery.findPackage(CsharpTemplateStandard.VALUE_OBJECT, contents);
  }

  private Set<String> resolveImports(final String basePackage, final CodeGenerationParameter aggregate,
                                     final List<TestCase> testCases, List<Content> contents) {
    final Set<String> imports = testCases.stream()
        .map(TestCase::involvedSpecialTypes)
        .flatMap(Set::stream)
        .map(CodeGenerationProperties.CSHARP_SPECIAL_TYPES_IMPORTS::get)
        .collect(toSet());

    imports.addAll(resolveMethodParameterImports(aggregate));
    imports.addAll(resolveAdapterImports(basePackage));
    imports.add(resolveValueObjectImports(aggregate, contents));
    imports.add(resolveModelImports(basePackage, aggregate));
    imports.add(resolveMockDispatcherImport(basePackage));
    return imports;
  }

  private String resolveModelImports(final String basePackage, final CodeGenerationParameter aggregate) {
    return AggregateDetail.resolvePackage(basePackage, aggregate.value);
  }

  private Optional<String> resolveDefaultFactoryMethodName(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .filter(method -> method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf))
        .map(method -> method.value)
        .findFirst();
  }

  private Set<String> resolveAdapterImports(final String basePackage) {
    return Collections.singleton(PersistenceDetail.resolvePackage(basePackage));
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.ENTITY_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(TemplateParameter.ENTITY_UNIT_TEST_NAME);
  }
}
