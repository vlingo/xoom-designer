// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.projections;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.Model.QUERY;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.StorageType.STATE_STORE;

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

    return TemplateParameters.with(PACKAGE_NAME, resolvePackage(basePackage)).and(MODEL, QUERY)
            .and(PROJECTION_NAME, projectionName).and(PROJECTION_TYPE, projectionType)
            .and(STATE_NAME, JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(protocolName))
            .and(STORAGE_TYPE, STATE_STORE).and(STATE_DATA_OBJECT_NAME, dataObjectName)
            .and(PROJECTION_TYPE, projectionType).and(PROJECTION_SOURCES, projectionSources)
            .andResolve(PROJECTION_SOURCE_TYPES_NAME, param -> JavaTemplateStandard.PROJECTION_SOURCE_TYPES.resolveClassname(param))
            .andResolve(STORE_PROVIDER_NAME, param -> JavaTemplateStandard.STORE_PROVIDER.resolveClassname(param))
            .addImports(resolveImports(dataObjectName, projectionSources, contents));
  }

  private Set<String> resolveImports(final String dataObjectName,
                                     final List<ProjectionSource> projectionSources,
                                     final List<Content> contents) {
    final Stream<String> defaultImports =
            Stream.of(ContentQuery.findPackage(JavaTemplateStandard.AGGREGATE_PROTOCOL, protocolName, contents),
                    ContentQuery.findPackage(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents))
                    .map(CodeElementFormatter::importAllFrom);

    final Stream<String> specialTypesImports =
            projectionSources.stream()
                    .map(ProjectionSource::getMergeParameters)
                    .map(mergeParameters -> CodeGenerationProperties.SPECIAL_TYPES.entrySet().stream().filter(entry -> mergeParameters.contains(entry.getKey())))
                    .flatMap(s -> s).map(involvedSpecialTypes -> involvedSpecialTypes.getValue());

    return Stream.of(defaultImports, specialTypesImports).flatMap(s -> s).collect(Collectors.toSet());
  }

  @SuppressWarnings("unused")
  private String retrieveProjectionSourceTypesQualifiedName(final List<TemplateData> templatesData) {
    return templatesData.stream().filter(data -> data.hasStandard(JavaTemplateStandard.PROJECTION_SOURCE_TYPES))
            .map(data -> data.parameters().<String>find(PROJECTION_SOURCE_TYPES_QUALIFIED_NAME))
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
