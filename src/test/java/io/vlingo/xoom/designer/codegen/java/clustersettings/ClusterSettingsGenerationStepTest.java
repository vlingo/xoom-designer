// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.clustersettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.TextBasedContent;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.ClusterSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClusterSettingsGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatCustomClusterSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.CLUSTER_SETTINGS, ClusterSettings.with(42333, 7));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ClusterSettingsGenerationStep().process(context);

    final Content clusterSettings =
            context.findContent(JavaTemplateStandard.CLUSTER_SETTINGS, "xoom-cluster");

    Assertions.assertEquals(((TextBasedContent)clusterSettings).text, (TextExpectation.onJava().read("custom-xoom-cluster")));
    Assertions.assertTrue(clusterSettings.contains(TextExpectation.onJava().read("custom-xoom-cluster")));
  }

  @Test
  public void testThatDefaultClusterSettingsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.CLUSTER_SETTINGS, ClusterSettings.with(0, 0));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ClusterSettingsGenerationStep().process(context);

    final Content clusterSettings =
            context.findContent(JavaTemplateStandard.CLUSTER_SETTINGS, "xoom-cluster");

    Assertions.assertEquals(((TextBasedContent)clusterSettings).text, (TextExpectation.onJava().read("default-xoom-cluster")));
    Assertions.assertTrue(clusterSettings.contains(TextExpectation.onJava().read("default-xoom-cluster")));
  }

}
