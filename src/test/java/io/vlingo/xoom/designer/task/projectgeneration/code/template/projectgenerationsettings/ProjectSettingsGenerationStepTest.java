// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.projectgenerationsettings;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.TextExpectation;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProjectSettingsGenerationStepTest {

  @Test
  public void testThatProjectSettingsIsGenerated() throws IOException {
    final String uglyProjectSettingsPayload =
            "{\"context\":{ \"groupId\":\"io.vlingo\", \"artifactId\":\"xoom-app\", \"artifactVersion\":\"1.0.0\"," +
                    " \"packageName\":\"io.vlingo.xoom-app\" }, \"deployment\":{ \"clusterNodes\":3, \"type\":\"NONE\", " +
                    "\"dockerImage\":\"xoom-app\", \"kubernetesImage\":\"vlingo/xoom-app\", \"kubernetesPod\":\"xoom-app\" }, " +
                    "\"projectDirectory\":\"/projects/\", \"useAnnotations\":false, \"useAutoDispatch\":false }";

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.APPLICATION_NAME, "xoom-app")
                    .add(Label.PROJECT_SETTINGS_PAYLOAD, uglyProjectSettingsPayload);

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters);

    new ProjectSettingsGenerationStep().process(context);

    final Content projectSettings = context.findContent(DesignerTemplateStandard.PROJECT_SETTINGS, "xoom-app-generation-settings");

    Assertions.assertTrue(projectSettings.contains(TextExpectation.onJava().read("project-settings").replace("\r", "")));
  }


}
