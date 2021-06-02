// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.turbosettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TurboSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.TURBO_SETTINGS;

public class TurboSettingsGenerationStepTest {

  @Test
  public void testThatCustomTurboSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(TURBO_SETTINGS, TurboSettings.with(18081, 9191));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new TurboSettingsGenerationStep().process(context);

    final Content turboSettings =
            context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("custom-xoom-turbo")));
  }

  @Test
  public void testThatDefaultTurboSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(TURBO_SETTINGS, TurboSettings.with(0, 0));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new TurboSettingsGenerationStep().process(context);

    final Content turboSettings =
            context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("default-xoom-turbo")));
  }

}
