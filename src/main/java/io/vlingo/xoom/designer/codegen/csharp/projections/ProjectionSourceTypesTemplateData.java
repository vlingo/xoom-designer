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
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.ProjectionSourceTypesDetail;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;

public class ProjectionSourceTypesTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static ProjectionSourceTypesTemplateData from(final String basePackage, final ProjectionType projectionType,
                                                       final List<Content> contents) {
    return new ProjectionSourceTypesTemplateData(basePackage, projectionType, contents);
  }

  private ProjectionSourceTypesTemplateData(final String basePackage, final ProjectionType projectionType,
                                            final List<Content> contents) {
    final String packageName = resolvePackage(basePackage);

    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.PROJECTION_TYPE, projectionType)
        .and(TemplateParameter.PROJECTION_SOURCES, ContentQuery.findClassNames(CsharpTemplateStandard.DOMAIN_EVENT, contents))
        .and(TemplateParameter.PROJECTION_SOURCE_TYPES_NAME, resolveClassName(projectionType))
        .andResolve(TemplateParameter.PROJECTION_SOURCE_TYPES_QUALIFIED_NAME, this::resolveQualifiedName);
  }

  private String resolvePackage(final String basePackage) {
    return ProjectionSourceTypesDetail.resolvePackage(basePackage);
  }

  private String resolveClassName(final ProjectionType projectionType) {
    return ProjectionSourceTypesDetail.resolveClassName(projectionType);
  }

  private String resolveQualifiedName(final TemplateParameters parameters) {
    final String packageName = parameters.find(TemplateParameter.PACKAGE_NAME);
    final String className = parameters.find(TemplateParameter.PROJECTION_SOURCE_TYPES_NAME);
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    return codeElementFormatter.qualifiedNameOf(packageName, className);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.PROJECTION_SOURCE_TYPES;
  }

  @Override
  public String filename() {
    return CsharpTemplateStandard.PROJECTION_SOURCE_TYPES.resolveFilename(parameters);
  }
}
