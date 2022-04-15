package io.vlingo.xoom.designer.codegen.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.TurboSettings;

import java.util.Arrays;
import java.util.List;

public class JavaApplicationSettingsData extends ApplicationSettingsData {

  public static final TemplateData NULL_TEMPLATE_DATA = new TemplateData() {
    @Override
    public TemplateParameters parameters() {
      return TemplateParameters.empty();
    }

    @Override
    public TemplateStandard standard() {
      return JavaTemplateStandard.LOGBACK_SETTINGS; // Need to define none TemplateStandard
    }
  };
  private final CodeGenerationContext context;

  public JavaApplicationSettingsData(CodeGenerationContext context) {
    this.context = context;
  }

  @Override
  List<TemplateData> projectSettings() {
    final TemplateData logbackSettings = BasicTemplateData.of(JavaTemplateStandard.LOGBACK_SETTINGS,
        TemplateParameters.with(TemplateParameter.RESOURCE_FILE, true));

    final TemplateData turboSettings = turboSettingsData(context);
    final TemplateData mavenSettings = mavenSettingsData(context);

    return Arrays.asList(turboSettings, mavenSettings, logbackSettings);
  }

  @Override
  TemplateData actorSettings() {
    return BasicTemplateData.of(JavaTemplateStandard.ACTOR_SETTINGS,
        TemplateParameters.with(TemplateParameter.RESOURCE_FILE, true));
  }

  @Override
  TemplateData readmeFile() {
    final String packageName = context.parameters().retrieveValue(Label.PACKAGE);

    final TemplateParameters templateParameters =
        TemplateParameters.with(TemplateParameter.README_FILE, true).and(TemplateParameter.PACKAGE_NAME, packageName);
    return BasicTemplateData.of(JavaTemplateStandard.README, templateParameters);
  }

  private TemplateData turboSettingsData(final CodeGenerationContext context) {
    final TurboSettings turboSettings = context.parameterObjectOf(Label.TURBO_SETTINGS);
    if(turboSettings == null)
      return NULL_TEMPLATE_DATA;

    final DeploymentSettings deploymentSettings = context.parameterObjectOf(Label.DEPLOYMENT_SETTINGS);

    return BasicTemplateData.of(JavaTemplateStandard.TURBO_SETTINGS,
        TemplateParameters.with(TemplateParameter.TURBO_SETTINGS, turboSettings)
        .and(TemplateParameter.DEPLOYMENT_SETTINGS, deploymentSettings)
        .and(TemplateParameter.RESOURCE_FILE, true));
  }

  private TemplateData mavenSettingsData(final CodeGenerationContext context) {
    return BasicTemplateData.of(JavaTemplateStandard.MAVEN_SETTINGS,
        TemplateParameters.with(TemplateParameter.POM_FILE, true)
        .and(TemplateParameter.GROUP_ID, context.parameterOf(Label.GROUP_ID))
        .and(TemplateParameter.ARTIFACT_ID, context.parameterOf(Label.ARTIFACT_ID))
        .and(TemplateParameter.ARTIFACT_VERSION, context.parameterOf(Label.ARTIFACT_VERSION))
        .and(TemplateParameter.XOOM_VERSION, context.parameterOf(Label.XOOM_VERSION))
        .and(TemplateParameter.APPLICATION_MAIN_CLASS, context.parameterOf(Label.APPLICATION_MAIN_CLASS)));
  }
}
