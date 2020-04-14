// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionStep;
import io.vlingo.xoom.starter.template.archetype.Archetype;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.template.TemplateGenerationException;
import io.vlingo.xoom.starter.template.Terminal;

import java.io.IOException;
import java.util.Properties;

import static io.vlingo.xoom.starter.template.archetype.ArchetypeProperties.TARGET_FOLDER;

public class CommandExecutionStep implements TaskExecutionStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "cd %s && mvn clean install && " +
            "cd %s && mvn archetype:generate -B -DarchetypeCatalog=internal %s";

    public void process(final TaskExecutionContext context) {
        try {
            final String[] commands = prepareCommands(context);
            final Process process = Runtime.getRuntime().exec(commands);
            context.followProcess(process);
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    protected String[] prepareCommands(final TaskExecutionContext context) {
        return new String[]{
                Terminal.active().initializationCommand(),
                Terminal.active().parameter(),
                archetypeCommands(context)};
    }

    private String archetypeCommands(final TaskExecutionContext context) {
        final Properties properties = context.properties();
        final Archetype archetype = Archetype.support(properties);
        final String targetFolder = context.propertyOf(TARGET_FOLDER);
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetype.folder(),
                targetFolder, archetype.fillMavenOptions(properties));
    }

}
