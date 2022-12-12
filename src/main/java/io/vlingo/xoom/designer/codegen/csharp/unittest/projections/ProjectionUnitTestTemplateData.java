// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectionUnitTestTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public ProjectionUnitTestTemplateData(final String basePackage, final ProjectionType projectionType,
                                        final CodeGenerationParameter aggregate, final List<Content> contents,
                                        final List<CodeGenerationParameter> valueObjects) {

    final CodeGenerationParameter signature = aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .filter(sig -> sig.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf))
        .findFirst()
        .orElse(aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).findFirst().get());

    final String projectionName = CsharpTemplateStandard.PROJECTION.resolveClassname(aggregate.value);
    final String entityName = CsharpTemplateStandard.AGGREGATE.resolveClassname(aggregate.value);
    final String domainEventName = signature.retrieveOneRelated(Label.DOMAIN_EVENT).value;
    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);
    final String aggregateState = CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value);

    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, resolvePackage(basePackage))
        .and(TemplateParameter.PROJECTION_UNIT_TEST_NAME, standard().resolveClassname(projectionName.replace("Actor", "")))
        .and(TemplateParameter.PROJECTION_NAME, projectionName)
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, entityName)
        .and(TemplateParameter.DOMAIN_EVENT_NAME, domainEventName)
        .and(TemplateParameter.DATA_OBJECT_NAME, dataObjectName)
        .and(TemplateParameter.STATE_DATA_OBJECT_NAME, aggregateState)
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, aggregate.value)
        .and(TemplateParameter.ENTITY_NAME, entityName)
        .and(TemplateParameter.STATE_NAME, aggregateState)
        .and(TemplateParameter.STATE_DATA_OBJECT_NAME, aggregateState)
        .and(TemplateParameter.TEST_CASES, TestCase.from(aggregate, valueObjects, projectionType))
        .and(TemplateParameter.PROJECTION_TYPE, projectionType)
        .addImport(resolveImport(CsharpTemplateStandard.DATA_OBJECT, contents))
        .addImport(ContentQuery.findPackage(CsharpTemplateStandard.AGGREGATE_STATE, aggregateState, contents))
        .addImport(resolveImport(CsharpTemplateStandard.PROJECTION, contents))
        .and(TemplateParameter.PRODUCTION_CODE, false)
        .and(TemplateParameter.UNIT_TEST, true);
  }

  public static List<TemplateData> from(final List<Content> contents, final String basePackage,
                                        final ProjectionType projectionType,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects) {
    final Function<CodeGenerationParameter, TemplateData> mapper = aggregate ->
        new ProjectionUnitTestTemplateData(basePackage, projectionType, aggregate, contents, valueObjects);

    return aggregates.stream()
        .map(mapper)
        .collect(Collectors.toList());
  }

  private static String resolvePackage(final String basePackage) {
    return basePackage + ".Tests.Infrastructure.Persistence";
  }

  private String resolveImport(final CsharpTemplateStandard dataObject, final List<Content> contents) {
    return ContentQuery.findPackage(dataObject, contents);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.PROJECTION_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(TemplateParameter.PROJECTION_UNIT_TEST_NAME);
  }

}
