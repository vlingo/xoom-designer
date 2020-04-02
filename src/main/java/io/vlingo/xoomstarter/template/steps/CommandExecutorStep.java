package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import io.vlingo.xoomstarter.template.TemplateGenerationException;

import java.io.IOException;

import static io.vlingo.xoomstarter.template.steps.PropertiesKeys.*;

public abstract class CommandExecutorStep implements TemplateGenerationStep {

    private static final String ARCHETYPE_COMMAND_PATTERN =
            "mvn archetype:generate -B " +
            "-DarchetypeGroupId=%s " +
            "-DarchetypeArtifactId=%s " +
            "-DarchetypeVersion=%s " +
            "-DgroupId=%s " +
            "-DartifactId=%s " +
            "-Dversion=%s " +
            "-Dpackage=%s";

    public void process(final TemplateGenerationContext context) {
        try {
            final String command = prepareCommand(context);
            final Process process = Runtime.getRuntime().exec(command);
            context.followProcess(process);
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    protected String prepareCommand(final TemplateGenerationContext context) {
        final String targetFolder = context.propertyOf(TARGET_FOLDER);
        final String archetypeCommand = prepareArchetypeCommand(context);
        return String.format(commandPattern(), targetFolder, archetypeCommand);
    }

    protected String prepareArchetypeCommand(final TemplateGenerationContext context) {
        return String.format(ARCHETYPE_COMMAND_PATTERN,
                context.archetype().groupId(),
                context.archetype().artifactId(),
                context.archetype().version(),
                context.propertyOf(GROUP_ID),
                context.propertyOf(ARTIFACT_ID),
                context.propertyOf(VERSION),
                context.propertyOf(PACKAGE)
        );
    }

    protected abstract String commandPattern();

}
