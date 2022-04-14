// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.projections;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class ProjectionSourceTypesDetail {

  private final static String INFRASTRUCTURE_PACKAGE = "infrastructure";

  public static String resolvePackage(final String basePackage) {
    return String.format("%s.%s", basePackage, INFRASTRUCTURE_PACKAGE).toLowerCase();
  }

  public static String resolveClassName(final ProjectionType projectionType) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.PROJECTION_TYPE, projectionType);
    return JavaTemplateStandard.PROJECTION_SOURCE_TYPES.resolveClassname(parameters);
  }

  public static String resolveQualifiedName(final String basePackage, final ProjectionType projectionType) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    return codeElementFormatter.qualifiedNameOf(resolvePackage(basePackage), resolveClassName(projectionType));
  }
}
