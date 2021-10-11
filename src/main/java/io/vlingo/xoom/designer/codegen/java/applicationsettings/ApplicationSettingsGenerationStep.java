// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.ProjectGenerationContext;
import io.vlingo.xoom.designer.codegen.ProjectGenerationStep;
import io.vlingo.xoom.designer.codegen.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.TurboSettings;

import java.util.Arrays;
import java.util.List;

public class ApplicationSettingsGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final TemplateData actorSettings =
            BasicTemplateData.of(JavaTemplateStandard.ACTOR_SETTINGS,
                    TemplateParameters.with(TemplateParameter.RESOURCE_FILE, true));

    final TemplateData logbackSettings =
            BasicTemplateData.of(JavaTemplateStandard.LOGBACK_SETTINGS,
                    TemplateParameters.with(TemplateParameter.RESOURCE_FILE, true));

    return Arrays.asList(turboSettingsData(context), mavenSettingsData(context),
            actorSettings, logbackSettings);
  }

  private TemplateData turboSettingsData(final CodeGenerationContext context) {
    final TurboSettings turboSettings =
            context.parameterObjectOf(Label.TURBO_SETTINGS);

    final DeploymentSettings deploymentSettings =
            context.parameterObjectOf(Label.DEPLOYMENT_SETTINGS);

    final TemplateParameters turboSettingsTemplateParameters =
            TemplateParameters.with(TemplateParameter.TURBO_SETTINGS, turboSettings)
                    .and(TemplateParameter.DEPLOYMENT_SETTINGS, deploymentSettings)
                    .and(TemplateParameter.RESOURCE_FILE, true);

    return BasicTemplateData.of(JavaTemplateStandard.TURBO_SETTINGS, turboSettingsTemplateParameters);
  }

  private TemplateData mavenSettingsData(final CodeGenerationContext context) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.POM_FILE, true)
                    .and(TemplateParameter.GROUP_ID, context.parameterOf(Label.GROUP_ID))
                    .and(TemplateParameter.ARTIFACT_ID, context.parameterOf(Label.ARTIFACT_ID))
                    .and(TemplateParameter.ARTIFACT_VERSION, context.parameterOf(Label.ARTIFACT_VERSION))
                    .and(TemplateParameter.XOOM_VERSION, context.parameterOf(Label.XOOM_VERSION))
                    .and(TemplateParameter.APPLICATION_MAIN_CLASS, context.parameterOf(Label.APPLICATION_MAIN_CLASS));

    return BasicTemplateData.of(JavaTemplateStandard.MAVEN_SETTINGS, parameters);
  }
}
