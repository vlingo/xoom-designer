// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.designermodel;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationTest;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DesignerModelGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatProjectSettingsIsGenerated() {
    final String uglyDesignerModel =
            "{\"context\":{ \"groupId\":\"io.vlingo\", \"artifactId\":\"xoom-app\", \"artifactVersion\":\"1.0.0\"," +
                    " \"packageName\":\"io.vlingo.xoom-app\" }, \"deployment\":{ \"clusterNodes\":3, \"type\":\"NONE\", " +
                    "\"dockerImage\":\"xoom-app\", \"kubernetesImage\":\"vlingo/xoom-app\", \"kubernetesPod\":\"xoom-app\" }, " +
                    "\"projectDirectory\":\"/projects/\", \"useAnnotations\":false, \"useAutoDispatch\":false }";

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.APPLICATION_NAME, "xoom-app")
                    .add(Label.DESIGNER_MODEL_JSON, uglyDesignerModel);

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters);

    new DesignerModelGenerationStep().process(context);

    final Content projectSettings = context.findContent(JavaTemplateStandard.DESIGNER_MODEL, "xoom-app-designer-model");

    Assertions.assertTrue(projectSettings.contains(TextExpectation.onJava().read("designer-model").replace("\r", "")));
  }


}
