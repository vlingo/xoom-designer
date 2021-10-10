// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.SchemataSettings;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;
import java.util.Optional;

public class SchemaPullStepTest {

  private TaskExecutionContext context;

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationOnWindows() {
    Infrastructure.setupResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER), 9019);
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.empty());
    context.with(loadGenerationParameters("E:\\projects\\designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPullStep(commandRetainer).execute(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PULL_COMMAND_ON_WINDOWS, commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testCommandPreparationWithProfileOnWindows() {
    Infrastructure.setupResources(HomeDirectory.from(WINDOWS_ROOT_FOLDER), 9019);
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.of(Tuple2.from("vlingo-xoom-schemata", 10009)));
    context.with(loadGenerationParameters("E:\\projects\\designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPullStep(commandRetainer).execute(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PULL_COMMAND_ON_WINDOWS + "-Pschemata-service", commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testCommandPreparationWithOnUnixBasedOS() {
    Infrastructure.setupResources(HomeDirectory.from(DEFAULT_ROOT_FOLDER), 9019);
    final SchemataSettings schemataSettings = SchemataSettings.with("localhost", 9019, Optional.empty());
    context.with(loadGenerationParameters("/home/projects/designer-example", schemataSettings));
    final CommandRetainer commandRetainer = new CommandRetainer();
    new SchemaPullStep(commandRetainer).execute(context);
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_SCHEMA_PULL_COMMAND, commandSequence[2]);
  }

  private CodeGenerationParameters loadGenerationParameters(final String targetFolder,
                                                            final SchemataSettings schemataSettings) {
    return CodeGenerationParameters.from(Label.SCHEMATA_SETTINGS, schemataSettings)
            .add(Label.TARGET_FOLDER, targetFolder);
  }

  @BeforeEach
  public void setUp() {
    Infrastructure.clear();
    this.context = TaskExecutionContext.bare().logger(Logger.noOpLogger());
  }

  private static final String WINDOWS_ROOT_FOLDER = Paths.get("D:", "tools", "designer").toString();

  private static final String DEFAULT_ROOT_FOLDER = Paths.get("home", "tools", "designer").toString();

  private static final String EXPECTED_SCHEMA_PULL_COMMAND_ON_WINDOWS =
          "E: && cd E:\\projects\\designer-example && mvnw.cmd io.vlingo.xoom:xoom-build-plugins:pull-schema@pull ";

  private static final String EXPECTED_SCHEMA_PULL_COMMAND =
          "cd /home/projects/designer-example && ./mvnw io.vlingo.xoom:xoom-build-plugins:pull-schema@pull ";

}
