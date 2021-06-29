// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.structure.archetype;

import io.vlingo.xoom.designer.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.Archetype;

import java.nio.file.Path;

public final class ArchetypeGenerationStep extends CommandExecutionStep {

  private final Archetype archetype;

  public ArchetypeGenerationStep(final Archetype archetype,
                                 final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
    this.archetype = archetype;
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final Terminal terminal = Terminal.supported();
    final Path temporaryTaskPath = ArchetypesFolder.path().resolve(context.executionId);
    final String directoryChangeCommand = terminal.resolveDirectoryChangeCommand(temporaryTaskPath);
    final String archetypeOptions = archetype.formatOptions(context.codeGenerationParameters());
    return String.format("%s && ..%s%s archetype:generate -B -DarchetypeCatalog=internal %s",
            directoryChangeCommand, terminal.pathSeparator(), terminal.mavenCommand(), archetypeOptions);
  }

}
