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

public class CsharpApplicationSettingsDataFactory extends ApplicationSettingsDataFactory {

  private final CodeGenerationContext context;

  public CsharpApplicationSettingsDataFactory(CodeGenerationContext context) {
    this.context = context;
  }

  @Override
  List<TemplateData> projectSettings() {
    final String appName = context.parameters().retrieveValue(Label.APPLICATION_NAME);
    final String sdkVersion = context.parameters().retrieveValue(Label.SDK_VERSION);
    final String vlingoVersion = context.parameters().retrieveValue(Label.VLINGO_VERSION);

    final TemplateData solutionSettings = BasicTemplateData.of(CsharpTemplateStandard.SOLUTION_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName));

    final TemplateData projectSettings = BasicTemplateData.of(CsharpTemplateStandard.PROJECT_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName)
        .and(TemplateParameter.PACKAGE_NAME, appName)
        .and(TemplateParameter.SDK_Version, sdkVersion)
        .and(TemplateParameter.VLINGO_VERSION, vlingoVersion));

    return Arrays.asList(solutionSettings, projectSettings);
  }

  @Override
  TemplateData actorSettings() {
    final String appName = context.parameters().retrieveValue(Label.APPLICATION_NAME);

    return BasicTemplateData.of(CsharpTemplateStandard.ACTOR_SETTINGS, TemplateParameters.with(TemplateParameter.PACKAGE_NAME, appName));
  }

}
