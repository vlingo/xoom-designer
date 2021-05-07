// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.PersistenceDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.TestDataValueGenerator;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.TestDataValueGenerator.TestDataValues;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter.qualifiedNameOf;
import static io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter.simpleNameToAttribute;
import static java.util.stream.Collectors.toSet;

public class EntityUnitTestTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage,
                                        final StorageType storageType,
                                        final ProjectionType projectionType,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final List<Content> contents) {
    return aggregates.stream().map(aggregate -> new EntityUnitTestTemplateData(basePackage, storageType, projectionType, aggregate, valueObjects, contents))
            .collect(Collectors.toList());
  }

  private EntityUnitTestTemplateData(final String basePackage,
                                     final StorageType storageType,
                                     final ProjectionType projectionType,
                                     final CodeGenerationParameter aggregate,
                                     final List<CodeGenerationParameter> valueObjects,
                                     final List<Content> contents) {
    final String entityName =
            AGGREGATE.resolveClassname(aggregate.value);

    final String stateName =
            AGGREGATE_STATE.resolveClassname(aggregate.value);

    final String packageName =
            ContentQuery.findPackage(AGGREGATE_PROTOCOL, aggregate.value, contents);

    final TestDataValues initialTestDataValues =
            TestDataValueGenerator.with(aggregate, valueObjects).generate();

    final Optional<String> defaultFactoryMethod =
            resolveDefaultFactoryMethodName(aggregate);

    final AuxiliaryEntityCreation auxiliaryEntityCreation =
            AuxiliaryEntityCreation.from(aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues);

    final List<TestCase> testCases =
            TestCase.from(aggregate, valueObjects, defaultFactoryMethod, initialTestDataValues, projectionType);

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName)
                    .and(DISPATCHER_NAME, MOCK_DISPATCHER.resolveClassname())
                    .and(AUXILIARY_ENTITY_CREATION, auxiliaryEntityCreation)
                    .and(ENTITY_CREATION_METHOD, AuxiliaryEntityCreation.METHOD_NAME)
                    .and(AGGREGATE_PROTOCOL_VARIABLE, simpleNameToAttribute(aggregate.value))
                    .and(ENTITY_UNIT_TEST_NAME, ENTITY_UNIT_TEST.resolveClassname(entityName))
                    .and(AGGREGATE_PROTOCOL_NAME, aggregate.value).and(ENTITY_NAME, entityName)
                    .and(STATE_NAME, stateName).and(ADAPTER_NAME, ADAPTER.resolveClassname(stateName))
                    .and(SOURCED_EVENTS, SourcedEvent.from(storageType, aggregate))
                    .and(STORAGE_TYPE, storageType).and(TEST_CASES, testCases)
                    .and(PRODUCTION_CODE, false).and(UNIT_TEST, true)
                    .addImport(resolveBaseEntryImport(projectionType))
                    .addImport(resolveMockDispatcherImport(basePackage))
                    .addImports(resolveMethodParameterImports(aggregate))
                    .addImport(resolveValueObjectImports(aggregate, contents))
                    .addImports(resolveAdapterImports(basePackage, storageType, aggregate));
  }

  private Set<String> resolveMethodParameterImports(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .map(method -> AggregateDetail.findInvolvedStateFields(aggregate, method.value))
            .map(involvedFields -> AggregateDetail.resolveImports(involvedFields))
            .flatMap(Set::stream).collect(Collectors.toSet());
  }

  private String resolveBaseEntryImport(final ProjectionType projectionType) {
    if (projectionType.isOperationBased()) {
      return "";
    }
    return BaseEntry.class.getCanonicalName();
  }

  private String resolveMockDispatcherImport(final String basePackage) {
    final String mockDispatcherName = MOCK_DISPATCHER.resolveClassname();
    final String mockDispatcherPackage = MockDispatcherDetail.resolvePackage(basePackage);
    return qualifiedNameOf(mockDispatcherPackage, mockDispatcherName);
  }

  private String resolveValueObjectImports(final CodeGenerationParameter aggregate, final List<Content> contents) {
    if (!ValueObjectDetail.useValueObject(aggregate)) {
      return "";
    }
    return CodeElementFormatter.importAllFrom(ContentQuery.findPackage(VALUE_OBJECT, contents));
  }

  private Set<String> resolveAdapterImports(final String basePackage,
                                            final StorageType storageType,
                                            final CodeGenerationParameter aggregate) {
    final String persistencePackage =
            PersistenceDetail.resolvePackage(basePackage);

    switch (storageType) {
      case STATE_STORE:
        final String stateName = AGGREGATE_STATE.resolveClassname(aggregate.value);
        final String adapterName = ADAPTER.resolveClassname(stateName);
        return Stream.of(qualifiedNameOf(persistencePackage, adapterName)).collect(toSet());
      case JOURNAL:
        return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT)
                .map(event -> ADAPTER.resolveClassname(event.value))
                .map(adapter -> qualifiedNameOf(persistencePackage, adapter))
                .collect(toSet());
      default:
        return Collections.emptySet();
    }
  }

  private Optional<String> resolveDefaultFactoryMethodName(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .filter(method -> method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf))
            .map(method -> method.value).findFirst();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.ENTITY_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(ENTITY_UNIT_TEST_NAME);
  }
}
