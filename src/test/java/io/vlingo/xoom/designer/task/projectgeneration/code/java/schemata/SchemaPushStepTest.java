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
import io.vlingo.xoom.designer.task.projectgeneration.code.java.SchemataSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;
import java.util.Optional;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.SCHEMATA_SETTINGS;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.TARGET_FOLDER;

public class SchemaPushStepTest {

  private TaskExecutionContext context;

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationOnWindows() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER));
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.empty());
    context.with(loadGenerationParameters("E:\\projects\\designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPushStep(commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PUSH_COMMAND_ON_WINDOWS, commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationWithProfileOnWindows() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER));
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.of("vlingo-xoom-schemata"));
    context.with(loadGenerationParameters("E:\\projects\\designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPushStep(commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PUSH_COMMAND_ON_WINDOWS + "-Pschemata-service", commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testCommandPreparationWithOnUnixBasedOS() {
    Infrastructure.resolveInternalResources(HomeDirectory.from(DEFAULT_ROOT_FOLDER));
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.empty());
    context.with(loadGenerationParameters("/home/projects/designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPushStep(commandRetainer).process(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PUSH_COMMAND, commandSequence[2]);
  }

  private CodeGenerationParameters loadGenerationParameters(final String targetFolder,
                                                            final SchemataSettings schemataSettings) {
    return CodeGenerationParameters.from(SCHEMATA_SETTINGS, schemataSettings)
            .add(TARGET_FOLDER, targetFolder);
  }

  @BeforeEach
  public void setUp() {
    Infrastructure.clear();
    this.context = TaskExecutionContext.empty().logger(Logger.noOpLogger());
  }

  private static final String WINDOWS_ROOT_FOLDER = Paths.get("D:", "tools", "designer").toString();

  private static final String DEFAULT_ROOT_FOLDER = Paths.get("home", "tools", "designer").toString();

  private static final String EXPECTED_SCHEMA_PUSH_COMMAND_ON_WINDOWS =
          "E: && cd E:\\projects\\designer-example && mvnw.cmd io.vlingo.xoom:xoom-build-plugins:push-schema@push ";

  private static final String EXPECTED_SCHEMA_PUSH_COMMAND =
          "cd /home/projects/designer-example && ./mvnw io.vlingo.xoom:xoom-build-plugins:push-schema@push ";

}
