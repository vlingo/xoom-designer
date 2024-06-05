// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.Arrays;
import java.util.List;

public class CsharpApplicationSettingsData extends ApplicationSettingsData {

  private final CodeGenerationContext context;

  public CsharpApplicationSettingsData(CodeGenerationContext context) {
    this.context = context;
  }

  @Override
  List<TemplateData> projectSettings() {
    final String solutionName = context.parameters().retrieveValue(Label.GROUP_ID);
    final String packageName = context.parameters().retrieveValue(Label.PACKAGE);
    final String sdkVersion = context.parameters().retrieveValue(Label.SDK_VERSION);
    final String xoomVersion = context.parameters().retrieveValue(Label.XOOM_VERSION);

    final TemplateData solutionSettings = BasicTemplateData.of(CsharpTemplateStandard.SOLUTION_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, solutionName)
            .and(TemplateParameter.ARTIFACT_ID, packageName));

    final TemplateData projectSettings = BasicTemplateData.of(CsharpTemplateStandard.PROJECT_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, packageName)
            .and(TemplateParameter.PACKAGE_NAME, packageName)
            .and(TemplateParameter.SDK_VERSION, sdkVersion)
            .and(TemplateParameter.XOOM_VERSION, xoomVersion));

    final TemplateData unitTestProjectSettings = BasicTemplateData.of(CsharpTemplateStandard.UNIT_TEST_PROJECT_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, packageName)
            .and(TemplateParameter.PACKAGE_NAME, packageName + ".Tests")
            .and(TemplateParameter.PRODUCTION_CODE, false)
            .and(TemplateParameter.UNIT_TEST, true)
            .and(TemplateParameter.SDK_VERSION, sdkVersion));

    return Arrays.asList(solutionSettings, projectSettings, unitTestProjectSettings);
  }

  @Override
  TemplateData actorSettings() {
    final String packageName = context.parameters().retrieveValue(Label.PACKAGE);
    return BasicTemplateData.of(CsharpTemplateStandard.ACTOR_SETTINGS,
        TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName));
  }

  @Override
  TemplateData readmeFile() {
    final String solutionName = context.parameters().retrieveValue(Label.GROUP_ID);

    final TemplateParameters templateParameters =
        TemplateParameters.with(TemplateParameter.README_FILE, true)
            .and(TemplateParameter.APPLICATION_NAME, solutionName);
    return BasicTemplateData.of(CsharpTemplateStandard.README, templateParameters);
  }

}
