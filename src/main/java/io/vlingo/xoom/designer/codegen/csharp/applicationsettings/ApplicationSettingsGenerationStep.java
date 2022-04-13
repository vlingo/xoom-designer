// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.Arrays;
import java.util.List;

public class ApplicationSettingsGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String appName = context.parameters().retrieveValue(Label.APPLICATION_NAME);
    final String sdkVersion = context.parameters().retrieveValue(Label.SDK_VERSION);
    final String vlingoVersion = context.parameters().retrieveValue(Label.VLINGO_VERSION);

    final TemplateData solutionSettings =
        BasicTemplateData.of(CsharpTemplateStandard.SOLUTION_SETTINGS,
            TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName));

    final TemplateData projectSettings =
        BasicTemplateData.of(CsharpTemplateStandard.PROJECT_SETTINGS,
            TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName)
                .and(TemplateParameter.PACKAGE_NAME, appName)
                .and(TemplateParameter.SDK_Version, sdkVersion)
                .and(TemplateParameter.VLINGO_VERSION, vlingoVersion));

    final TemplateData actorSettings =
        BasicTemplateData.of(CsharpTemplateStandard.ACTOR_SETTINGS,
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, appName));

    return Arrays.asList(solutionSettings, projectSettings, actorSettings);
  }

  @Override
  protected Dialect resolveDialect(CodeGenerationContext context) {
    return Dialect.withName(context.parameters().retrieveValue(Label.DIALECT));
  }
}
