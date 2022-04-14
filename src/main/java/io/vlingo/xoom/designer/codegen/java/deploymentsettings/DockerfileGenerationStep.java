// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
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

public class DockerfileGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final TemplateParameters templateParameters =
            TemplateParameters.with(TemplateParameter.ARTIFACT_ID, context.parameterOf(Label.ARTIFACT_ID))
            .and(TemplateParameter.DOCKERFILE, true);

    return Arrays.asList(BasicTemplateData.of(JavaTemplateStandard.DOCKERFILE, templateParameters));
  }

  @Override
  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.DOCKER;
  }

  @Override
  public boolean shouldProcess(CodeGenerationContext context) {
    return ((DeploymentSettings) context.parameterObjectOf(Label.DEPLOYMENT_SETTINGS)).useDocker;
  }

}
