// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.clustersettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.ClusterSettings;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.CLUSTER_SETTINGS;

public class ClusterSettingsGenerationStepTest {

  @Test
  public void testThatCustomClusterSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(CLUSTER_SETTINGS, ClusterSettings.of(8081, 42333, 7));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ClusterSettingsGenerationStep().process(context);

    final Content clusterSettings =
            context.findContent(JavaTemplateStandard.CLUSTER_SETTINGS, "xoom-cluster");

    Assertions.assertTrue(clusterSettings.contains(TextExpectation.onJava().read("custom-xoom-cluster")));
  }

  @Test
  public void testThatDefaultClusterSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(CLUSTER_SETTINGS, ClusterSettings.of(0, 0, 0));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ClusterSettingsGenerationStep().process(context);

    final Content clusterSettings =
            context.findContent(JavaTemplateStandard.CLUSTER_SETTINGS, "xoom-cluster");

    Assertions.assertTrue(clusterSettings.contains(TextExpectation.onJava().read("default-xoom-cluster")));
  }

}
