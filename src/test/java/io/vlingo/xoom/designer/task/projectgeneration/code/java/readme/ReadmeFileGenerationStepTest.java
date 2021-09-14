// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.readme;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationTest;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadmeFileGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatReadmeFileIsGenerated() {
    final CodeGenerationContext context =
            CodeGenerationContext.with(CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp"));

    new ReadmeFileGenerationStep().process(context);

    final Content readme =
            context.findContent(JavaTemplateStandard.README, "README");

    Assertions.assertTrue(readme.contains(TextExpectation.onJava().read("readme")));
  }

}
