// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.projections;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;

public class ProjectionSourceTypesDetail {

  private final static String INFRASTRUCTURE_PACKAGE = "infrastructure";

  public static String resolvePackage(final String basePackage) {
    return String.format("%s.%s", basePackage, INFRASTRUCTURE_PACKAGE).toLowerCase();
  }

  public static String resolveClassName(final ProjectionType projectionType) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.PROJECTION_TYPE, projectionType);
    return DesignerTemplateStandard.PROJECTION_SOURCE_TYPES.resolveClassname(parameters);
  }

  public static String resolveQualifiedName(final String basePackage, final ProjectionType projectionType) {
    return CodeElementFormatter.qualifiedNameOf(resolvePackage(basePackage), resolveClassName(projectionType));
  }
}
