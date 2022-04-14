package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.applicationsettings.ApplicationSettingsGenerationStep;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TurboSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationSettingsGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatCustomTurboSettingsAreGenerated() {
    final DeploymentSettings deploymentSettings =
        DeploymentSettings.with(DeploymentType.KUBERNETES, "xoom-app",
            "xoom-app-img", "xoom-app-pod", 9898);

    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(Label.TURBO_SETTINGS, TurboSettings.with(18081, 9191))
            .add(CodeGenerationParameter.ofObject(Label.DEPLOYMENT_SETTINGS, deploymentSettings));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ApplicationSettingsGenerationStep().process(context);

    final Content turboSettings =
        context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    final Content actorSettings =
        context.findContent(JavaTemplateStandard.ACTOR_SETTINGS, "xoom-actors");

    final Content logbackSettings =
        context.findContent(JavaTemplateStandard.LOGBACK_SETTINGS, "logback");

    final Content mavenSettings =
        context.findContent(JavaTemplateStandard.MAVEN_SETTINGS, "pom");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("custom-xoom-turbo")));
    Assertions.assertTrue(actorSettings.contains(TextExpectation.onJava().read("xoom-actors")));
    Assertions.assertTrue(logbackSettings.contains(TextExpectation.onJava().read("logback")));
    Assertions.assertTrue(mavenSettings.contains(TextExpectation.onJava().read("pom")));
  }

  @Test
  public void testThatDefaultTurboSettingsAreGenerated() {
    final DeploymentSettings deploymentSettings =
        DeploymentSettings.with(DeploymentType.KUBERNETES, "xoom-app",
            "xoom-app-img", "xoom-app-pod", 9898);

    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(Label.TURBO_SETTINGS, TurboSettings.with(0, 0))
            .add(CodeGenerationParameter.ofObject(Label.DEPLOYMENT_SETTINGS, deploymentSettings));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ApplicationSettingsGenerationStep().process(context);

    final Content turboSettings =
        context.findContent(JavaTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    final Content actorSettings =
        context.findContent(JavaTemplateStandard.ACTOR_SETTINGS, "xoom-actors");

    final Content logbackSettings =
        context.findContent(JavaTemplateStandard.LOGBACK_SETTINGS, "logback");

    final Content mavenSettings =
        context.findContent(JavaTemplateStandard.MAVEN_SETTINGS, "pom");

    Assertions.assertTrue(turboSettings.contains(TextExpectation.onJava().read("default-xoom-turbo")));
    Assertions.assertTrue(actorSettings.contains(TextExpectation.onJava().read("xoom-actors")));
    Assertions.assertTrue(logbackSettings.contains(TextExpectation.onJava().read("logback")));
    Assertions.assertTrue(mavenSettings.contains(TextExpectation.onJava().read("pom")));
  }

  @Test
  public void testThatCsharpSolutionIsGenerated() {
    final String namespace = "Io.Vlingo.Xoomapp";
    final String appName = "Xoomapp";
    final String sdkVersion = "net6.0";
    final String vlingoVersion = "1.9.3";

    final CodeGenerationParameters parameters = CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, namespace),
            CodeGenerationParameter.of(Label.APPLICATION_NAME, appName),
            CodeGenerationParameter.of(Label.SDK_VERSION, sdkVersion),
            CodeGenerationParameter.of(Label.VLINGO_VERSION, vlingoVersion),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new ApplicationSettingsGenerationStep().process(context);

    final Content solutionSettings =
        context.findContent(CsharpTemplateStandard.SOLUTION_SETTINGS, "Xoomapp");

    final Content projectSettings =
        context.findContent(CsharpTemplateStandard.PROJECT_SETTINGS, "Xoomapp");

    final Content actorSettings =
        context.findContent(CsharpTemplateStandard.ACTOR_SETTINGS, "vlingo-actors");

    Assertions.assertTrue(solutionSettings.contains(TextExpectation.onCSharp().read("solution")));
    Assertions.assertTrue(projectSettings.contains(TextExpectation.onCSharp().read("project")));
    Assertions.assertTrue(actorSettings.contains(TextExpectation.onCSharp().read("xoom-actors")));
  }
}
