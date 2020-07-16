// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.template.archetype.Archetype;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.TARGET_FOLDER;

public class ArchetypeCommandResolverStep extends CommandResolverStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "%s && mvn clean install && " +
            "%s && mvn archetype:generate -B -DarchetypeCatalog=internal %s";

    @Override
    protected String formatCommands(TaskExecutionContext context) {
        final Archetype archetype = Archetype.KUBERNETES;
        final Properties properties = context.properties();
        final String targetFolder = context.propertyOf(TARGET_FOLDER);
        final String archetypeFolderCommand = resolveDirectoryChangeCommand(archetype.folder());
        final String targetFolderCommand = resolveDirectoryChangeCommand(targetFolder);
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetypeFolderCommand,
                targetFolderCommand, archetype.fillMavenOptions(properties));
    }

}
