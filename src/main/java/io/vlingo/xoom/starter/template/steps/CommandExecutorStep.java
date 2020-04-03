package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationException;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;

import java.io.IOException;

import static io.vlingo.xoom.starter.template.steps.PropertiesKeys.*;

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
            final String[] commands = prepareCommands(context);
            final Process process = Runtime.getRuntime().exec(commands);
            context.followProcess(process);
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    protected abstract String[] prepareCommands(final TemplateGenerationContext context);

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

}
