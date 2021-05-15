package io.vlingo.xoom.designer;

import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.content.ContentCreationStep;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.DefaultCommandExecutionProcess;
import io.vlingo.xoom.designer.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.autodispatch.AutoDispatchMappingGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.bootstrap.BootstrapGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.dataobject.DataObjectGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.ExchangeGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.ModelGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.ValueObjectGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projectgenerationsettings.ProjectSettingsGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.resource.RestResourceGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.schemata.SchemataGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty.EntityUnitTestGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.queries.QueriesUnitTestGenerationStep;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.BrowserLaunchCommandExecutionStep;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.GenerationTargetRegistrationStep;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.UserInterfaceBootstrapStep;
import io.vlingo.xoom.designer.task.projectgeneration.steps.*;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.ComponentRegistry.withType;

public class Configuration {

  private static final int DEFAULT_REQUEST_LIMIT = 10;
  public static final String REQUEST_LIMIT = "REQUEST_LIMIT";
  public static final String MAVEN_WRAPPER_DIRECTORY = ".mvn";
  public static final String XOOM_DESIGNER_FILE_VERSION = "1.7.8";
  private static final String XOOM_VERSION_PLACEHOLDER = "1.7.8-SNAPSHOT";
  public static final String REQUEST_COUNT_EXPIRATION = "REQUEST_COUNT_DURATION";
  private static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_DESIGNER_HOME";
  private static final String ENVIRONMENT_TYPE_VARIABLE = "VLINGO_XOOM_DESIGNER_ENV";
  private static final Duration DEFAULT_REQUEST_COUNT_EXPIRATION = Duration.ofSeconds(1);

  static {
    ComponentRegistry.register(CommandExecutionProcess.class, new DefaultCommandExecutionProcess());
  }

  public static final List<TaskExecutionStep> PROJECT_GENERATION_STEPS = Arrays.asList(
          new ResourcesLocationStep(),
          new CodeGenerationParametersLoadStep(),
          new CodeGenerationParameterValidationStep(),
          new MainClassResolverStep(),
          new ArchetypeFolderCleanUpStep(),
          new TemporaryTaskFolderCreationStep(),
          new ArchetypeInstallationStep(Archetype.findDefault(), withType(CommandExecutionProcess.class)),
          new ArchetypeGenerationStep(Archetype.findDefault(), withType(CommandExecutionProcess.class)),
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

  public static Environment resolveEnvironment() {
    final String environment = System.getenv(ENVIRONMENT_TYPE_VARIABLE);
    return environment == null ? Environment.LOCAL : Environment.CLOUD;
  }

  public static int resolveProjectGenerationRequestLimit() {
    final String requestLimit = System.getenv(REQUEST_LIMIT);
    return requestLimit != null ? Integer.valueOf(requestLimit) : DEFAULT_REQUEST_LIMIT;
  }

  public static Duration resolveProjectGenerationRequestCountExpiration() {
    final String expirationSeconds = System.getenv(REQUEST_COUNT_EXPIRATION);
    return expirationSeconds != null ? Duration.ofSeconds(Long.valueOf(expirationSeconds)) : DEFAULT_REQUEST_COUNT_EXPIRATION;
  }

}
