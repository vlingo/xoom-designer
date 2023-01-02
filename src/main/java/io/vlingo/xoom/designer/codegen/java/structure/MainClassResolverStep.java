// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.structure;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.annotation.codegen.AnnotationBasedTemplateStandard;

public class MainClassResolverStep implements CodeGenerationStep {

  private static final String QUALIFIED_PATTERN = "%s.infrastructure.%s";

  @Override
  public void process(final CodeGenerationContext context) {
    final String basePackage =
            context.parameterOf(Label.PACKAGE);

    final Boolean useAnnotations =
            Boolean.valueOf(context.parameterOf(Label.USE_ANNOTATIONS));

    final String mainClass =
            String.format(QUALIFIED_PATTERN, basePackage, resolveClassName(useAnnotations));

    context.parameters().add(Label.APPLICATION_MAIN_CLASS, mainClass);
  }

  private String resolveClassName(final Boolean useAnnotations) {
    return useAnnotations ? AnnotationBasedTemplateStandard.XOOM_INITIALIZER.resolveClassname() :
            JavaTemplateStandard.BOOTSTRAP.resolveClassname();
  }

}
