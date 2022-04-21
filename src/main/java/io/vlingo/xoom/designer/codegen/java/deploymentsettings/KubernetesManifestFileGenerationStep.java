// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.Arrays;
import java.util.List;

public class KubernetesManifestFileGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final DeploymentSettings deploymentSettings =
            context.parameterObjectOf(Label.DEPLOYMENT_SETTINGS);

    final TemplateParameters templateParameters =
            TemplateParameters.with(TemplateParameter.KUBERNETES_POD_NAME, deploymentSettings.kubernetesPod)
                    .and(TemplateParameter.KUBERNETES_IMAGE, deploymentSettings.kubernetesImage);

    return Arrays.asList(BasicTemplateData.of(JavaTemplateStandard.KUBERNETES_MANIFEST_FILE, templateParameters));
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final DeploymentSettings kubernetes = context.parameterObjectOf(Label.DEPLOYMENT_SETTINGS);
    return kubernetes != null && kubernetes.useKubernetes;
  }

}
