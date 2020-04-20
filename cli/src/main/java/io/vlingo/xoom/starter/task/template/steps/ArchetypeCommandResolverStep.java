package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.archetype.Archetype;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.TARGET_FOLDER;

public class ArchetypeCommandResolverStep extends CommandResolverStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "cd %s && mvn clean install && " +
            "cd %s && mvn archetype:generate -B -DarchetypeCatalog=internal %s";

    @Override
    protected String formatCommands(TaskExecutionContext context) {
        final Properties properties = context.properties();
        final Archetype archetype = Archetype.support(properties);
        final String targetFolder = context.propertyOf(TARGET_FOLDER);
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetype.folder(),
                targetFolder, archetype.fillMavenOptions(properties));
    }

}
