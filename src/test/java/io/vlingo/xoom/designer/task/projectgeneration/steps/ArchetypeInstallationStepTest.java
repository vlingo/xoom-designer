// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.archetype.Archetype;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class ArchetypeInstallationStepTest {

  private TaskExecutionContext context;

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationWithKubernetesArchetypeOnWindows() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER));
    context.with(loadGenerationParameters("E:\\projects"));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new ArchetypeInstallationStep(Archetype.KUBERNETES, commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(String.format(EXPECTED_COMMAND_ON_WINDOWS, context.executionId), commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testCommandPreparationWithKubernetesArchetypeOnUnixBasedOS() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(DEFAULT_ROOT_FOLDER));
    context.with(loadGenerationParameters("/home/projects"));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new ArchetypeInstallationStep(Archetype.KUBERNETES,commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(String.format(EXPECTED_COMMAND, context.executionId), commandSequence[2]);
  }

  private CodeGenerationParameters loadGenerationParameters(final String targetFolder) {
    return CodeGenerationParameters.from(VERSION, "1.0")
            .add(GROUP_ID, "io.vlingo").add(ARTIFACT_ID, "designer-example")
            .add(PACKAGE, "io.vlingo.designerexample").add(XOOM_VERSION, "1.2.9")
            .add(TARGET_FOLDER, targetFolder).add(DOCKER_IMAGE, "designer-example-image")
            .add(KUBERNETES_IMAGE, "designer-example-image").add(KUBERNETES_POD_NAME, "designer-example-pod")
            .add(MAIN_CLASS, "io.vlingo.designerexample.infrastructure.Bootstrap");
  }

  @BeforeEach
  public void setUp() {
    Infrastructure.clear();
    this.context = TaskExecutionContext.withoutOptions();
  }

  @AfterEach
  public void clear() {
    Terminal.disable();
  }

  private static final String WINDOWS_ROOT_FOLDER = Paths.get("D:", "tools", "designer").toString();

  private static final String DEFAULT_ROOT_FOLDER = Paths.get("home", "tools", "designer").toString();

  private static final String WINDOWS_ARCHETYPE_PATH =
          Paths.get(WINDOWS_ROOT_FOLDER, "resources", "archetypes").toString();

  private static final String DEFAULT_ARCHETYPE_PATH =
          Paths.get(DEFAULT_ROOT_FOLDER, "resources", "archetypes").toString();

  private static final String EXPECTED_COMMAND_ON_WINDOWS =
          "D: && cd " + WINDOWS_ARCHETYPE_PATH + " && mvnw.cmd -f kubernetes-archetype\\pom.xml clean install";

  private static final String EXPECTED_COMMAND =
          "cd " + DEFAULT_ARCHETYPE_PATH + " && ./mvnw -f ./kubernetes-archetype/pom.xml clean install";

}
