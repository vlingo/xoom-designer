package io.vlingo.xoom.designer.codegen.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
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
    final String projectName = context.parameters().retrieveValue(Label.ARTIFACT_ID);
    final String sdkVersion = context.parameters().retrieveValue(Label.SDK_VERSION);
    final String xoomVersion = context.parameters().retrieveValue(Label.XOOM_VERSION);

    final TemplateData solutionSettings = BasicTemplateData.of(CsharpTemplateStandard.SOLUTION_SETTINGS,
        TemplateParameters.with(TemplateParameter.APPLICATION_NAME, solutionName));

    final TemplateData projectSettings = BasicTemplateData.of(CsharpTemplateStandard.PROJECT_SETTINGS,
        namespaceFrom(TemplateParameter.APPLICATION_NAME)
            .and(TemplateParameter.PACKAGE_NAME, projectName)
            .and(TemplateParameter.SDK_Version, sdkVersion)
            .and(TemplateParameter.XOOM_VERSION, xoomVersion));

    return Arrays.asList(solutionSettings, projectSettings);
  }

  @Override
  TemplateData actorSettings() {
    return BasicTemplateData.of(CsharpTemplateStandard.ACTOR_SETTINGS, namespaceFrom(TemplateParameter.PACKAGE_NAME));
  }

  private TemplateParameters namespaceFrom(TemplateParameter packageName) {
    final String projectName = context.parameters().retrieveValue(Label.ARTIFACT_ID);
    return TemplateParameters.with(packageName, projectName);
  }

  @Override
  TemplateData readmeFile() {
    final String appName = context.parameters().retrieveValue(Label.GROUP_ID);

    final TemplateParameters templateParameters =
        TemplateParameters.with(TemplateParameter.README_FILE, true).and(TemplateParameter.PACKAGE_NAME, appName);
    return BasicTemplateData.of(JavaTemplateStandard.README, templateParameters);
  }

}
