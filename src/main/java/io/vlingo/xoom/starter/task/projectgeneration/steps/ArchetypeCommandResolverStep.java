// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.Terminal;
import io.vlingo.xoom.starter.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class ArchetypeCommandResolverStep extends CommandResolverStep {

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final Terminal terminal = Terminal.supported();
        final Archetype defaultArchetype = Archetype.findDefault();
        final String archetypeFolderCommand = resolveDirectoryChangeCommand(ArchetypesFolder.path().toString());
        final String archetypeOptions = defaultArchetype.formatOptions(context.codeGenerationParameters());
        return String.format("%s && %s -f %s clean install && %s archetype:generate -B -DarchetypeCatalog=internal %s",
                archetypeFolderCommand, terminal.mavenCommand(), defaultArchetype.resolvePomPath(),
                terminal.mavenCommand(), archetypeOptions);
    }

    @Override
    protected List<File> executableFiles() {
        return Arrays.asList(Terminal.supported().executableMavenFileLocation());
    }

}
