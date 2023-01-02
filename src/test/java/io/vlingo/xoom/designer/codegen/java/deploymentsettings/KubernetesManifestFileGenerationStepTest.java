// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
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
import io.vlingo.xoom.designer.codegen.DeploymentType;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KubernetesManifestFileGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatManifestFileIsGenerated() {
    final DeploymentSettings deploymentSettings =
            DeploymentSettings.with(DeploymentType.KUBERNETES, "xoom-app", "xoom-app-img", "xoom-app-pod", 9898);

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.DEPLOYMENT_SETTINGS, deploymentSettings);

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters);

    new KubernetesManifestFileGenerationStep().process(context);

    final Content manifestFile =
            context.findContent(JavaTemplateStandard.KUBERNETES_MANIFEST_FILE, "xoom-app-pod");

    Assertions.assertTrue(manifestFile.contains(TextExpectation.onJava().read("k8s-manifest-file")));
  }
}
