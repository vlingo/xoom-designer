// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.projections;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.PROJECTION_TO_DESCRIPTION;

public class ProjectionDispatcherProviderTemplateData extends TemplateData {

  private static final String PACKAGE_PATTERN = "%s.%s.%s";
  private static final String PARENT_PACKAGE_NAME = "Infrastructure";
  private static final String PERSISTENCE_PACKAGE_NAME = "Persistence";

  private final TemplateParameters templateParameters;
  private final boolean placeholder;

  public static ProjectionDispatcherProviderTemplateData from(final String basePackage,
                                                              final ProjectionType projectionType,
                                                              final List<Content> contents) {
    return new ProjectionDispatcherProviderTemplateData(basePackage, projectionType, contents);
  }

  private ProjectionDispatcherProviderTemplateData(final String basePackage, final ProjectionType projectionType,
                                                   final List<Content> contents) {
    final String packageName = resolvePackage(basePackage);

    final List<ProjectToDescription> projectToDescriptionEntries = ProjectToDescription.from(projectionType, contents);

    this.templateParameters = TemplateParameters.with(PACKAGE_NAME, packageName)
        .and(PROJECTION_TO_DESCRIPTION, projectToDescriptionEntries)
        .addImports(resolveImports(basePackage, projectionType, contents));

    this.placeholder = false;
  }

  private String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME);
  }

  private Set<String> resolveImports(final String basePackage, final ProjectionType projectionType,
                                     final List<Content> contents) {
    return ContentQuery.findClassNames(CsharpTemplateStandard.DOMAIN_EVENT, contents)
        .stream().map(className -> ContentQuery.findPackage(CsharpTemplateStandard.DOMAIN_EVENT, className, contents))
        .collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER;
  }

  @Override
  public boolean isPlaceholder() {
    return placeholder;
  }

}
