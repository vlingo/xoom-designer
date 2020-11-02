// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;

import static io.vlingo.xoom.codegen.parameter.Label.TARGET_FOLDER;

public class ArchetypeCommandResolverStep extends CommandResolverStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "%s && mvn clean install && " +
            "%s && mvn archetype:generate -B -DarchetypeCatalog=internal %s";

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final Archetype defaultArchetype = Archetype.findDefault();
        final String targetFolder = context.codeGenerationParameters().retrieveValue(TARGET_FOLDER);
        final String archetypeFolderCommand = resolveDirectoryChangeCommand(defaultArchetype.folder());
        final String targetFolderCommand = resolveDirectoryChangeCommand(targetFolder);
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetypeFolderCommand,
                targetFolderCommand, defaultArchetype.formatOptions(context.codeGenerationParameters()));
    }

}
