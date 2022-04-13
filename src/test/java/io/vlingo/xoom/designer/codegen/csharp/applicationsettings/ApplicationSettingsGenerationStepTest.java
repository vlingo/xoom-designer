package io.vlingo.xoom.designer.codegen.csharp.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationSettingsGenerationStepTest {

  @Test
  public void testThatSolutionIsGenerated() {

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
