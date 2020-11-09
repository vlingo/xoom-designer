// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.Terminal;
import io.vlingo.xoom.starter.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;

import static io.vlingo.xoom.codegen.parameter.Label.ARTIFACT_ID;
import static io.vlingo.xoom.codegen.parameter.Label.TARGET_FOLDER;

public class ArchetypeCommandResolverStep extends CommandResolverStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "%s && %s -f %s clean install && " +
            "%s archetype:generate -B -DarchetypeCatalog=internal %s&& %s %s %s";

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final Terminal terminal = Terminal.supported();
        final Archetype defaultArchetype = Archetype.findDefault();
        final String projectRootFolder = context.codeGenerationParameters().retrieveValue(ARTIFACT_ID);
        final String targetFolder = context.codeGenerationParameters().retrieveValue(TARGET_FOLDER);
        final String archetypeFolderCommand = resolveDirectoryChangeCommand(Resource.ARCHETYPES_FOLDER.path());
        final String archetypeOptions = defaultArchetype.formatOptions(context.codeGenerationParameters());
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetypeFolderCommand, terminal.mavenCommand(),
                defaultArchetype.resolvePomPath(), terminal.mavenCommand(), archetypeOptions,
                terminal.moveCommand(), projectRootFolder, targetFolder);
    }

}
