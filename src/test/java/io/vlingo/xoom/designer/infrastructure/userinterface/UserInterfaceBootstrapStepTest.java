package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutor;
import io.vlingo.xoom.designer.task.projectgeneration.Archetype;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.structure.archetype.ArchetypeInstallationStep;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static io.vlingo.xoom.designer.Configuration.ENVIRONMENT_TYPE_VARIABLE;

public class UserInterfaceBootstrapStepTest {

  @Test
  public void testThatUserInterfaceBootstrapStepIsProcessedOnLocalEnvironment() {
    ComponentRegistry.register(ENVIRONMENT_TYPE_VARIABLE, "LOCAL");
    final CommandRetainer commandRetainer = new CommandRetainer();
    final ArchetypeInstallationStep archetypeInstallationStep = new ArchetypeInstallationStep(Archetype.findDefault(), commandRetainer);
    ComponentRegistry.register(ArchetypeInstallationStep.class, archetypeInstallationStep);
    new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
    Assertions.assertFalse(TaskExecutor.shouldExit());
    Assertions.assertTrue(commandRetainer.retainedCommandsSequence().isEmpty());
  }

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testThatContainerizedUserInterfaceBootstrapStepIsProcessedOnWindows() {
    ComponentRegistry.register(ENVIRONMENT_TYPE_VARIABLE, "CONTAINER");
    final CommandRetainer commandRetainer = new CommandRetainer();
    final ArchetypeInstallationStep archetypeInstallationStep = new ArchetypeInstallationStep(Archetype.findDefault(), commandRetainer);
    ComponentRegistry.register(ArchetypeInstallationStep.class, archetypeInstallationStep);
    new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
    Assertions.assertFalse(TaskExecutor.shouldExit());
    Assertions.assertFalse(commandRetainer.retainedCommandsSequence().isEmpty());
    Assertions.assertTrue(commandRetainer.retainedCommandsSequence().get(0)[2].endsWith("mvnw.cmd -f kubernetes-archetype\\pom.xml clean install"));
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testThatContainerizedUserInterfaceBootstrapStepIsProcessedOnUnixBasedOS() {
    ComponentRegistry.register(ENVIRONMENT_TYPE_VARIABLE, "CONTAINER");
    final CommandRetainer commandRetainer = new CommandRetainer();
    final ArchetypeInstallationStep archetypeInstallationStep = new ArchetypeInstallationStep(Archetype.findDefault(), commandRetainer);
    ComponentRegistry.register(ArchetypeInstallationStep.class, archetypeInstallationStep);
    new UserInterfaceBootstrapStep().process(TaskExecutionContext.withoutOptions());
    Assertions.assertFalse(TaskExecutor.shouldExit());
    Assertions.assertFalse(commandRetainer.retainedCommandsSequence().isEmpty());
    Assertions.assertTrue(commandRetainer.retainedCommandsSequence().get(0)[2].endsWith("./mvnw -f ./kubernetes-archetype/pom.xml clean install"));
  }

  @BeforeEach
  public void setUp() {
    Infrastructure.clear();
    ComponentRegistry.clear();
    Profile.enableTestProfile();
    Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    ComponentRegistry.register(GenerationTarget.class, GenerationTarget.FILESYSTEM);
  }

  @AfterEach
  public void tearDown() throws Exception {
    XoomInitializer.instance().stopServer();
    XoomInitializer.instance().terminateWorld();
  }

  @AfterAll
  public static void clear() {
    Profile.disableTestProfile();
  }
}
