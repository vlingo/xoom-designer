package io.vlingo.xoom.designer;

import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.ContentCreationStep;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.designer.codegen.CodeGenerationParameterValidationStep;
import io.vlingo.xoom.designer.codegen.StagingFolderCleanUpStep;
import io.vlingo.xoom.designer.codegen.StagingFolderCleanUpStep.Phase;
import io.vlingo.xoom.designer.codegen.TemporaryTaskFolderCreationStep;
import io.vlingo.xoom.designer.codegen.applicationsettings.ApplicationSettingsGenerationStep;
import io.vlingo.xoom.designer.codegen.designermodel.DesignerModelGenerationStep;
import io.vlingo.xoom.designer.codegen.java.autodispatch.AutoDispatchMappingGenerationStep;
import io.vlingo.xoom.designer.codegen.java.bootstrap.BootstrapGenerationStep;
import io.vlingo.xoom.designer.codegen.java.clustersettings.ClusterSettingsGenerationStep;
import io.vlingo.xoom.designer.codegen.java.dataobject.DataObjectGenerationStep;
import io.vlingo.xoom.designer.codegen.java.deploymentsettings.DockerComposeGenerationStep;
import io.vlingo.xoom.designer.codegen.java.deploymentsettings.DockerfileGenerationStep;
import io.vlingo.xoom.designer.codegen.java.deploymentsettings.KubernetesManifestFileGenerationStep;
import io.vlingo.xoom.designer.codegen.java.exchange.ExchangeGenerationStep;
import io.vlingo.xoom.designer.codegen.java.model.ModelGenerationStep;
import io.vlingo.xoom.designer.codegen.java.model.ValueObjectGenerationStep;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionGenerationStep;
import io.vlingo.xoom.designer.codegen.java.resource.RestResourceGenerationStep;
import io.vlingo.xoom.designer.codegen.java.schemata.SchemaPullStep;
import io.vlingo.xoom.designer.codegen.java.schemata.SchemaPushStep;
import io.vlingo.xoom.designer.codegen.java.schemata.SchemataGenerationStep;
import io.vlingo.xoom.designer.codegen.java.storage.StorageGenerationStep;
import io.vlingo.xoom.designer.codegen.java.structure.MainClassResolverStep;
import io.vlingo.xoom.designer.codegen.java.structure.MavenWrapperInstallationStep;
import io.vlingo.xoom.designer.codegen.java.unittest.entity.EntityUnitTestGenerationStep;
import io.vlingo.xoom.designer.codegen.java.unittest.projections.ProjectionUnitTestGenerationStep;
import io.vlingo.xoom.designer.codegen.java.unittest.queries.QueriesUnitTestGenerationStep;
import io.vlingo.xoom.designer.codegen.java.unittest.resource.RestResourceAbstractUnitTestGenerationStep;
import io.vlingo.xoom.designer.codegen.java.unittest.resource.RestResourceUnitTestGenerationStep;
import io.vlingo.xoom.designer.codegen.reactjs.AggregateManagementGenerationStep;
import io.vlingo.xoom.designer.codegen.reactjs.LayoutGenerationStep;
import io.vlingo.xoom.designer.codegen.reactjs.StaticFilesGenerationStep;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.vlingo.xoom.turbo.ComponentRegistry.withType;

public class Configuration {

  private static final int DEFAULT_REQUEST_LIMIT = 10;
  public static final String REQUEST_LIMIT = "REQUEST_LIMIT";
  public static final String MAVEN_WRAPPER_DIRECTORY = ".mvn";
  public static final String XOOM_DESIGNER_FILE_VERSION = "1.7.8";
  private static final String XOOM_VERSION_PLACEHOLDER = "{{XOOM_VERSION}}";
  public static final String REQUEST_COUNT_EXPIRATION = "REQUEST_COUNT_DURATION";
  private static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_DESIGNER_HOME";
  public static final String ENVIRONMENT_TYPE_VARIABLE = "VLINGO_XOOM_DESIGNER_ENV";
  public static final String SCHEMATA_SERVICE_NAME = "SCHEMATA_SERVICE_NAME";
  public static final String SCHEMATA_SERVICE_PORT = "SCHEMATA_SERVICE_PORT";
  private static final Duration DEFAULT_REQUEST_COUNT_EXPIRATION = Duration.ofSeconds(1);

  public static void load() {
    final CodeElementFormatter defaultCodeElementFormatter =
        CodeElementFormatter.with(Dialect.findDefault(),
            ReservedWordsHandler.usingSuffix("_"));

    ComponentRegistry.register("defaultCodeFormatter", defaultCodeElementFormatter);
    final CodeElementFormatter cSharpCodeElementFormatter =
        CodeElementFormatter.with(Dialect.C_SHARP,
            ReservedWordsHandler.usingSuffix("_"));

    ComponentRegistry.register("defaultCodeFormatter", defaultCodeElementFormatter);
    ComponentRegistry.register("cSharpCodeFormatter", cSharpCodeElementFormatter);
    ComponentRegistry.register("codeGenerationSteps", codeGenerationSteps());
    ComponentRegistry.register("cSharpCodeGenerationSteps", cSharpCodeGenerationSteps());
  }

  private static List<CodeGenerationStep> codeGenerationSteps() {
    return Arrays.asList(
        // Preliminary
        new CodeGenerationParameterValidationStep(),
        new MainClassResolverStep(),
        new StagingFolderCleanUpStep(Phase.PRE_GENERATION),
        new TemporaryTaskFolderCreationStep(),
        // CodeGen
        new ApplicationSettingsGenerationStep(),
        new DesignerModelGenerationStep(),
        // JAVA
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
        new EntityUnitTestGenerationStep(),
        new QueriesUnitTestGenerationStep(),
        new ProjectionUnitTestGenerationStep(),
        new RestResourceAbstractUnitTestGenerationStep(),
        new RestResourceUnitTestGenerationStep(),
        new ClusterSettingsGenerationStep(),
        new DockerfileGenerationStep(),
        new DockerComposeGenerationStep(),
        new KubernetesManifestFileGenerationStep(),
        // React
        new StaticFilesGenerationStep(),
        new LayoutGenerationStep(),
        new AggregateManagementGenerationStep(),
        // Concluding
        new ContentCreationStep(),
        new MavenWrapperInstallationStep(),
        new SchemaPushStep(withType(CommandExecutionProcess.class)),
        new SchemaPullStep(withType(CommandExecutionProcess.class)),
        new StagingFolderCleanUpStep(Phase.POST_GENERATION)
    );
  }

  private static List<CodeGenerationStep> cSharpCodeGenerationSteps() {
    return Arrays.asList(
        // Preliminary
        new CodeGenerationParameterValidationStep(),
        new StagingFolderCleanUpStep(Phase.PRE_GENERATION),
        new TemporaryTaskFolderCreationStep(),
        // CodeGen
        new ApplicationSettingsGenerationStep(),
        new DesignerModelGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.model.ValueObjectGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.model.ModelGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.storage.StorageGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.unittest.entity.EntityUnitTestGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.unittest.projections.ProjectionUnitTestGenerationStep(),
        new io.vlingo.xoom.designer.codegen.csharp.bootstrap.BootstrapGenerationStep(),
        // Concluding
        new ContentCreationStep(),
        new StagingFolderCleanUpStep(Phase.POST_GENERATION)
    );
  }

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
    final String registeredEnv = ComponentRegistry.withName(ENVIRONMENT_TYPE_VARIABLE);
    if(registeredEnv != null) {
      return Environment.valueOf(registeredEnv);
    }
    final String environment = System.getenv(ENVIRONMENT_TYPE_VARIABLE);
    return environment == null ? Environment.LOCAL : Environment.valueOf(environment);
  }

  public static int resolveProjectGenerationRequestLimit() {
    final String requestLimit = System.getenv(REQUEST_LIMIT);
    return requestLimit != null ? Integer.parseInt(requestLimit) : DEFAULT_REQUEST_LIMIT;
  }

  public static Duration resolveProjectGenerationRequestCountExpiration() {
    final String expirationSeconds = System.getenv(REQUEST_COUNT_EXPIRATION);
    return expirationSeconds != null ? Duration.ofSeconds(Long.parseLong(expirationSeconds)) : DEFAULT_REQUEST_COUNT_EXPIRATION;
  }

  public static Optional<Tuple2<String, Integer>> resolveSchemataServiceDNS() {
    final String schemataServiceName = System.getenv(SCHEMATA_SERVICE_NAME);
    if(schemataServiceName != null) {
      final String schemataServicePort = System.getenv(SCHEMATA_SERVICE_PORT);
      if (schemataServicePort == null) {
        return Optional.of(Tuple2.tuple(schemataServiceName, null));
      }
      if(!StringUtils.isNumeric(schemataServicePort)) {
        throw new IllegalArgumentException("The schemata service port is not a number.");
      }
      return Optional.of(Tuple2.tuple(schemataServiceName, Integer.parseInt(schemataServicePort)));
    }
    return Optional.empty();
  }

}
