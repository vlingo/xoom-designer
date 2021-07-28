// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.DeploymentType;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.DeploymentSettings;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TurboSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.TURBO_SETTINGS;

public class ApplicationSettingsGenerationStepTest {

  @Test
  public void testThatCustomTurboSettingsAreGenerated() {
    final DeploymentSettings deploymentSettings =
            DeploymentSettings.with(DeploymentType.KUBERNETES, "xoom-app",
                    "xoom-app-img", "xoom-app-pod", 9898);

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(TURBO_SETTINGS, TurboSettings.with(18081, 9191))
                    .add(CodeGenerationParameter.ofObject(Label.DEPLOYMENT_SETTINGS, deploymentSettings));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ApplicationSettingsGenerationStep().process(context);

    final Content turboSettings =
            context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    final Content actorSettings =
            context.findContent(JavaTemplateStandard.ACTOR_SETTINGS, "xoom-actors");

    final Content logbackSettings =
            context.findContent(JavaTemplateStandard.LOGBACK_SETTINGS, "logback");

    final Content mavenSettings =
            context.findContent(JavaTemplateStandard.MAVEN_SETTINGS, "pom");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("custom-xoom-turbo")));
    Assertions.assertTrue(actorSettings.contains(TextExpectation.onJava().read("xoom-actors")));
    Assertions.assertTrue(logbackSettings.contains(TextExpectation.onJava().read("logback")));
    Assertions.assertTrue(mavenSettings.contains(TextExpectation.onJava().read("pom")));
  }

  @Test
  public void testThatDefaultTurboSettingsAreGenerated() {
    final DeploymentSettings deploymentSettings =
            DeploymentSettings.with(DeploymentType.KUBERNETES, "xoom-app",
                    "xoom-app-img", "xoom-app-pod", 9898);

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(TURBO_SETTINGS, TurboSettings.with(0, 0))
                    .add(CodeGenerationParameter.ofObject(Label.DEPLOYMENT_SETTINGS, deploymentSettings));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ApplicationSettingsGenerationStep().process(context);

    final Content turboSettings =
            context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    final Content actorSettings =
            context.findContent(JavaTemplateStandard.ACTOR_SETTINGS, "xoom-actors");

    final Content logbackSettings =
            context.findContent(JavaTemplateStandard.LOGBACK_SETTINGS, "logback");

    final Content mavenSettings =
            context.findContent(JavaTemplateStandard.MAVEN_SETTINGS, "pom");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("default-xoom-turbo")));
    Assertions.assertTrue(actorSettings.contains(TextExpectation.onJava().read("xoom-actors")));
    Assertions.assertTrue(logbackSettings.contains(TextExpectation.onJava().read("logback")));
    Assertions.assertTrue(mavenSettings.contains(TextExpectation.onJava().read("pom")));
  }

}
