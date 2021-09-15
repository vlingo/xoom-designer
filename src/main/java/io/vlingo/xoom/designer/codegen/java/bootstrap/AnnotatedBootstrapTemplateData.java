// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

public class AnnotatedBootstrapTemplateData extends BootstrapTemplateData {

  private static final String RESOURCES_ANNOTATION_QUALIFIED_NAME = "io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers";

  @Override
  protected void enrichParameters(final CodeGenerationContext context) {
    if (ContentQuery.exists(JavaTemplateStandard.REST_RESOURCE, context.contents())) {
      parameters().addImport(RESOURCES_ANNOTATION_QUALIFIED_NAME);
    }

    parameters().and(TemplateParameter.REST_RESOURCE_PACKAGE, ContentQuery.findPackage(JavaTemplateStandard.REST_RESOURCE, context.contents()));
  }

  @Override
  protected boolean support(CodeGenerationContext context) {
    return context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf);
  }

}
