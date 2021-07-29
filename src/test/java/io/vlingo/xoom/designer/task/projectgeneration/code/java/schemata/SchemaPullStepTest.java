// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.TARGET_FOLDER;

public class SchemaPullStepTest {

  private TaskExecutionContext context;

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationOnWindows() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER));
    context.with(loadGenerationParameters("E:\\projects\\designer-example"));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPullStep(commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(String.format(EXPECTED_SCHEMA_PULL_COMMAND_ON_WINDOWS, context.executionId), commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testCommandPreparationWithOnUnixBasedOS() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(DEFAULT_ROOT_FOLDER));
    context.with(loadGenerationParameters("/home/projects/designer-example"));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPullStep(commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(String.format(EXPECTED_SCHEMA_PULL_COMMAND, context.executionId), commandSequence[2]);
  }

  private CodeGenerationParameters loadGenerationParameters(final String targetFolder) {
    return CodeGenerationParameters.from(TARGET_FOLDER, targetFolder);
  }

  @BeforeEach
  public void setUp() {
    Infrastructure.clear();
    this.context = TaskExecutionContext.empty().logger(Logger.noOpLogger());
  }

  private static final String WINDOWS_ROOT_FOLDER = Paths.get("D:", "tools", "designer").toString();

  private static final String DEFAULT_ROOT_FOLDER = Paths.get("home", "tools", "designer").toString();

  private static final String EXPECTED_SCHEMA_PULL_COMMAND_ON_WINDOWS =
          "E: && cd E:\\projects\\designer-example && mvnw.cmd io.vlingo.xoom:xoom-build-plugins:pull-schema@pull";

  private static final String EXPECTED_SCHEMA_PULL_COMMAND =
          "cd /home/projects/designer-example && ./mvnw io.vlingo.xoom:xoom-build-plugins:pull-schema@pull";

}
