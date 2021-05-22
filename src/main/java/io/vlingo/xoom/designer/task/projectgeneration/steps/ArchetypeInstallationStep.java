// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.Archetype;
import io.vlingo.xoom.designer.task.CommandExecutionStep;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ArchetypeInstallationStep extends CommandExecutionStep {

  private final Archetype archetype;

  public ArchetypeInstallationStep(final Archetype archetype,
                                   final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
    this.archetype = archetype;
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final Terminal terminal = Terminal.supported();
    final Path archetypeFolderPath = ArchetypesFolder.path();
    final String archetypeFolderCommand = terminal.resolveDirectoryChangeCommand(archetypeFolderPath);
    return String.format("%s && %s -f %s clean install",
            archetypeFolderCommand, terminal.mavenCommand(), archetype.resolvePomPath());
  }

  @Override
  protected List<File> executableFiles() {
    return Arrays.asList(Terminal.supported().executableMavenFileLocation());
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return !archetype.jarPath(ArchetypesFolder.path()).toFile().exists();
  }
}
