// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.clustersettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter;
import io.vlingo.xoom.designer.task.projectgeneration.parameters.ClusterSettings;

import java.util.Arrays;
import java.util.List;

public class ClusterSettingsGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final ClusterSettings clusterSettings =
            context.parameterObjectOf(Label.CLUSTER_SETTINGS);

    final TemplateParameters clusterSettingsTemplateParameters =
            TemplateParameters.with(TemplateParameter.CLUSTER_SETTINGS, clusterSettings)
                    .and(TemplateParameter.RESOURCE_FILE, true);

    final TemplateData clusterSettingsTemplateData =
            BasicTemplateData.of(JavaTemplateStandard.CLUSTER_SETTINGS, clusterSettingsTemplateParameters);

    return Arrays.asList(clusterSettingsTemplateData);
  }

}
