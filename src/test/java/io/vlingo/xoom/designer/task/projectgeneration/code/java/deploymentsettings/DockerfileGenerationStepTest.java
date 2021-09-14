// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationTest;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.ARTIFACT_ID;

public class DockerfileGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatDockerfileIsGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(ARTIFACT_ID, "xoom-app");

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters);

    new DockerfileGenerationStep().process(context);

    final Content manifestFile =
            context.findContent(JavaTemplateStandard.DOCKERFILE, "Dockerfile");

    Assertions.assertTrue(manifestFile.contains(TextExpectation.onJava().read("dockerfile")));
  }

}
