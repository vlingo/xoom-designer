package io.vlingo.xoom.designer;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.DefaultCommandExecutionProcess;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.BrowserLaunchCommandExecutionStep;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.GenerationTargetRegistrationStep;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.UserInterfaceBootstrapStep;
import io.vlingo.xoom.designer.task.projectgeneration.steps.*;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;
import io.vlingo.xoom.turbo.codegen.CodeGenerationStep;
import io.vlingo.xoom.turbo.codegen.content.ContentCreationStep;
import io.vlingo.xoom.turbo.codegen.template.autodispatch.AutoDispatchMappingGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.bootstrap.BootstrapGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.dataobject.DataObjectGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.exchange.ExchangeGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.model.ModelGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.model.ValueObjectGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.projectgenerationsettings.ProjectSettingsGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.projections.ProjectionGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.resource.RestResourceGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.schemata.SchemataGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.storage.StorageGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.unittest.entity.EntityUnitTestGenerationStep;
import io.vlingo.xoom.turbo.codegen.template.unittest.queries.QueriesUnitTestGenerationStep;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.ComponentRegistry.withType;

public class Configuration {

  public static final String MAVEN_WRAPPER_DIRECTORY = ".mvn";
  public static final String XOOM_DESIGNER_FILE_VERSION = "1.7.0";
  private static final String XOOM_VERSION_PLACEHOLDER = "1.7.7-SNAPSHOT";
  private static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_DESIGNER_HOME";

  static {
    ComponentRegistry.register(CommandExecutionProcess.class, new DefaultCommandExecutionProcess());
  }

  public static final List<TaskExecutionStep> PROJECT_GENERATION_STEPS = Arrays.asList(
          new ResourcesLocationStep(),
          new CodeGenerationParametersLoadStep(),
          new CodeGenerationParameterValidationStep(),
          new MainClassResolverStep(),
          new ArchetypeFolderCleanUpStep(),
          new ArchetypeCommandExecutionStep(withType(CommandExecutionProcess.class)),
          new ProjectInstallationStep(),
          new MavenWrapperInstallationStep(),
          new CodeGenerationExecutionerStep(),
          new ContentPurgerStep(),
          new ProjectCompressionStep(),
          new ArchetypeFolderCleanUpStep()
  );

  public static final List<CodeGenerationStep> CODE_GENERATION_STEPS = Arrays.asList(
          new ValueObjectGenerationStep(),
          new ModelGenerationStep(),
          new DataObjectGenerationStep(),
          new ProjectionGenerationStep(),
          new StorageGenerationStep(),
          new RestResourceGenerationStep(),
          new AutoDispatchMappingGenerationStep(),
          new ExchangeGenerationStep(),
          new SchemataGenerationStep(),
          new BootstrapGenerationStep(),
          new ProjectSettingsGenerationStep(),
          new EntityUnitTestGenerationStep(),
          new QueriesUnitTestGenerationStep(),
          new ContentCreationStep()
  );

  public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
          new ResourcesLocationStep(), new GenerationTargetRegistrationStep(), new UserInterfaceBootstrapStep(),
          new BrowserLaunchCommandExecutionStep(withType(CommandExecutionProcess.class))
  );

  public static String resolveDefaultXoomVersion() {
    final String version = Configuration.class.getPackage().getImplementationVersion();
    if (version == null) {
      System.out.println("Unable to find default VLINGO XOOM version. Using development version: " + XOOM_VERSION_PLACEHOLDER);
      return XOOM_VERSION_PLACEHOLDER;
    }
    return version;
  }

  public static String resolveHomePath() {
    if (Profile.isTestProfileEnabled()) {
      return Paths.get(System.getProperty("user.dir"), "dist", "designer").toString();
    }
    return System.getenv(Configuration.HOME_ENVIRONMENT_VARIABLE);
  }

}
