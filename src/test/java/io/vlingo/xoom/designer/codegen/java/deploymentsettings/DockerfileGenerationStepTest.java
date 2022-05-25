// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DockerfileGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatDockerfileIsGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.ARTIFACT_ID, "xoom-app");

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters);

    new DockerfileGenerationStep().process(context);

    final Content manifestFile =
            context.findContent(JavaTemplateStandard.DOCKERFILE, "Dockerfile");

    Assertions.assertTrue(manifestFile.contains(TextExpectation.onJava().read("dockerfile")));
  }

  @Test
  public void testThatDockerComposeIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(Label.ARTIFACT_ID, "xoom-app");

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters);

    new DockerComposeGenerationStep().process(context);

    final Content dockerCompose =
        context.findContent(JavaTemplateStandard.DOCKER_COMPOSE, "docker-compose");

    Assertions.assertTrue(dockerCompose.contains(TextExpectation.onJava().read("docker-compose")));
  }

}
