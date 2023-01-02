// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.projections;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.storage.Model.QUERY;
import static io.vlingo.xoom.designer.codegen.java.storage.StorageType.STATE_STORE;

public class ProjectionTemplateData extends TemplateData {

  private static final String PACKAGE_PATTERN = "%s.%s.%s";
  private static final String PARENT_PACKAGE_NAME = "infrastructure";
  private static final String PERSISTENCE_PACKAGE_NAME = "persistence";

  private final String protocolName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage,
                                        final Stream<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final ProjectionType projectionType,
                                        final List<Content> contents) {
    return aggregates.map(aggregate -> {
      return new ProjectionTemplateData(basePackage, aggregate, valueObjects, projectionType, contents);
    }).collect(Collectors.toList());
  }

  private ProjectionTemplateData(final String basePackage,
                                 final CodeGenerationParameter aggregate,
                                 final List<CodeGenerationParameter> valueObjects,
                                 final ProjectionType projectionType,
                                 final List<Content> contents) {
    this.protocolName = aggregate.value;
    this.parameters = loadParameters(basePackage, aggregate,
            valueObjects, projectionType, contents);

  }

  private TemplateParameters loadParameters(final String basePackage,
                                            final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> valueObjects,
                                            final ProjectionType projectionType,
                                            final List<Content> contents) {
    final String projectionName = JavaTemplateStandard.PROJECTION.resolveClassname(protocolName);
    final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(protocolName);

    final List<CodeGenerationParameter> events =
            aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).collect(Collectors.toList());

    final List<ProjectionSource> projectionSources =
            ProjectionSource.from(projectionType, aggregate, events, valueObjects);

    return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, resolvePackage(basePackage)).and(TemplateParameter.MODEL, QUERY)
            .and(TemplateParameter.PROJECTION_NAME, projectionName).and(TemplateParameter.PROJECTION_TYPE, projectionType)
            .and(TemplateParameter.STATE_NAME, JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(protocolName))
            .and(TemplateParameter.STORAGE_TYPE, STATE_STORE).and(TemplateParameter.STATE_DATA_OBJECT_NAME, dataObjectName)
            .and(TemplateParameter.PROJECTION_TYPE, projectionType).and(TemplateParameter.PROJECTION_SOURCES, projectionSources)
            .andResolve(TemplateParameter.PROJECTION_SOURCE_TYPES_NAME, param -> JavaTemplateStandard.PROJECTION_SOURCE_TYPES.resolveClassname(param))
            .andResolve(TemplateParameter.STORE_PROVIDER_NAME, param -> JavaTemplateStandard.STORE_PROVIDER.resolveClassname(param))
            .addImports(resolveImports(dataObjectName, projectionSources, contents));
  }

  private Set<String> resolveImports(final String dataObjectName,
                                     final List<ProjectionSource> projectionSources,
                                     final List<Content> contents) {
    final CodeElementFormatter codeElementFormatter =
            ComponentRegistry.withName("defaultCodeFormatter");

    final Stream<String> defaultImports =
            Stream.of(ContentQuery.findPackage(JavaTemplateStandard.AGGREGATE_PROTOCOL, protocolName, contents),
                    ContentQuery.findPackage(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents))
                    .map(codeElementFormatter::importAllFrom);

    final Stream<String> specialTypesImports =
            projectionSources.stream()
                    .map(projectionSource -> projectionSource.mergeParameters)
                    .flatMap(mergeParameters -> CodeGenerationProperties.SPECIAL_TYPES_IMPORTS.entrySet().stream().filter(entry -> mergeParameters.contains(entry.getKey()))).map(involvedSpecialTypes -> involvedSpecialTypes.getValue());

    return Stream.of(defaultImports, specialTypesImports).flatMap(s -> s).collect(Collectors.toSet());
  }

  @SuppressWarnings("unused")
  private String retrieveProjectionSourceTypesQualifiedName(final List<TemplateData> templatesData) {
    return templatesData.stream().filter(data -> data.hasStandard(JavaTemplateStandard.PROJECTION_SOURCE_TYPES))
            .map(data -> data.parameters().<String>find(TemplateParameter.PROJECTION_SOURCE_TYPES_QUALIFIED_NAME))
            .findFirst().get();
  }

  private String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME).toLowerCase();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.PROJECTION;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(protocolName, parameters);
  }
}
