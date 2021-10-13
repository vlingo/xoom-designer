// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.structure;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainClassResolverStepTest {

  @Test
  public void testThatDefaultMainClassIsResolved() {
    final CodeGenerationContext context = buildContext(false);
    new MainClassResolverStep().process(context);
    final String mainClass = context.parameterOf(Label.APPLICATION_MAIN_CLASS);
    Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.Bootstrap", mainClass);
  }

  @Test
  public void testThatAnnotatedMainClassIsResolved() {
    final CodeGenerationContext context = buildContext(true);
    new MainClassResolverStep().process(context);
    final String mainClass = context.parameterOf(Label.APPLICATION_MAIN_CLASS);
    Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.XoomInitializer", mainClass);
  }

  private CodeGenerationContext buildContext(final Boolean useAnnotations) {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.USE_ANNOTATIONS, useAnnotations);

    return CodeGenerationContext.with(parameters);
  }

}
