// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.projections;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.storage.Model.QUERY;
import static io.vlingo.xoom.designer.codegen.csharp.storage.StorageType.STATE_STORE;

public class ProjectionTemplateData extends TemplateData {

  private static final String PACKAGE_PATTERN = "%s.%s.%s";
  private static final String PARENT_PACKAGE_NAME = "Infrastructure";
  private static final String PERSISTENCE_PACKAGE_NAME = "Persistence";

  private final String protocolName;
  private final String aggregateName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage, final Stream<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final ProjectionType projectionType, final List<Content> contents) {
    return aggregates.map(aggregate -> new ProjectionTemplateData(basePackage, aggregate, valueObjects,
            projectionType, contents))
        .collect(Collectors.toList());
  }

  private ProjectionTemplateData(final String basePackage, final CodeGenerationParameter aggregate,
                                 final List<CodeGenerationParameter> valueObjects,
                                 final ProjectionType projectionType, final List<Content> contents) {
    this.protocolName = AggregateDetail.resolveProtocolNameFor(aggregate);
    this.aggregateName = aggregate.value;
    this.parameters = loadParameters(basePackage, aggregate, valueObjects, projectionType, contents);
  }

  private TemplateParameters loadParameters(final String basePackage, final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> valueObjects,
                                            final ProjectionType projectionType, final List<Content> contents) {
    final String projectionName = CsharpTemplateStandard.PROJECTION.resolveClassname(aggregateName);
    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);

    final List<CodeGenerationParameter> events =
        aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).collect(Collectors.toList());

    final List<ProjectionSource> projectionSources =
        ProjectionSource.from(projectionType, aggregate, events, valueObjects);

    return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, resolvePackage(basePackage)).and(TemplateParameter.MODEL, QUERY)
        .and(TemplateParameter.PROJECTION_NAME, projectionName).and(TemplateParameter.PROJECTION_TYPE, projectionType)
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregateName))
        .and(TemplateParameter.STORAGE_TYPE, STATE_STORE).and(TemplateParameter.STATE_DATA_OBJECT_NAME, dataObjectName)
        .and(TemplateParameter.PROJECTION_TYPE, projectionType).and(TemplateParameter.PROJECTION_SOURCES, projectionSources)
        .andResolve(TemplateParameter.PROJECTION_SOURCE_TYPES_NAME, CsharpTemplateStandard.PROJECTION_SOURCE_TYPES::resolveClassname)
        .andResolve(TemplateParameter.STORE_PROVIDER_NAME, CsharpTemplateStandard.STORE_PROVIDER::resolveClassname)
        .addImports(resolveImports(dataObjectName, projectionSources, contents));
  }

  private Set<String> resolveImports(final String dataObjectName, final List<ProjectionSource> projectionSources,
                                     final List<Content> contents) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final String packageOf = codeElementFormatter.packageOf(ContentQuery.findFullyQualifiedClassName(CsharpTemplateStandard.AGGREGATE_PROTOCOL, protocolName, contents));
    final Stream<String> defaultImports =
        Stream.of(packageOf, ContentQuery.findPackage(CsharpTemplateStandard.DATA_OBJECT, dataObjectName, contents));

    final Stream<String> specialTypesImports = projectionSources.stream()
        .map(projectionSource -> projectionSource.mergeParameters)
        .flatMap(mergeParameters -> CodeGenerationProperties.CSHARP_SPECIAL_TYPES_IMPORTS.entrySet().stream()
            .filter(entry -> mergeParameters.contains(entry.getKey())))
        .map(Map.Entry::getValue);

    return Stream.of(defaultImports, specialTypesImports).flatMap(s -> s).collect(Collectors.toSet());
  }

  private String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.PROJECTION;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateName, parameters);
  }
}
