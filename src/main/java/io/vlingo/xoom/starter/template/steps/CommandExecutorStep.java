package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.archetype.Archetype;
import io.vlingo.xoom.starter.archetype.ArchetypeProperties;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import io.vlingo.xoom.starter.template.TemplateGenerationException;

import java.io.IOException;
import java.util.Properties;

public class CommandExecutorStep implements TemplateGenerationStep {

    private static final String ARCHETYPE_COMMANDS_PATTERN = "cd %s && mvn clean install && cd %s && mvn archetype:generate -B %s";

    public void process(final TemplateGenerationContext context) {
        try {
            final String[] commands = prepareCommands(context);
            final Process process = Runtime.getRuntime().exec(commands);
            context.followProcess(process);
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    protected String[] prepareCommands(final TemplateGenerationContext context) {
        return new String[]{
                Terminal.active().initializationCommand(),
                Terminal.active().parameter(),
                archetypeCommands(context)};
    }

    private String archetypeCommands(final TemplateGenerationContext context) {
        final Properties properties = context.properties();
        final Archetype archetype = Archetype.support(properties);
        final String targetFolder = context.propertyOf(ArchetypeProperties.TARGET_FOLDER);
        return String.format(ARCHETYPE_COMMANDS_PATTERN, archetype.folder(),
                targetFolder, archetype.fillMavenOptions(properties));
    }

}
