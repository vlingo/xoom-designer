package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public class DefaultCommandExecutorStep extends CommandExecutorStep {

    private static final String UNSUPPORTED_OPERATING_SYSTEM = "Windows";

    @Override
    protected String[] prepareCommands(final TemplateGenerationContext context) {
        final String targetFolder = context.propertyOf(PropertiesKeys.TARGET_FOLDER);
        final String archetypeCommand = prepareArchetypeCommand(context);
        return new String[]{"sh", "-c", "cd " + targetFolder + " && " + archetypeCommand};
    }

    @Override
    public boolean shouldProcess() {
        return !System.getProperty("os.name").contains(UNSUPPORTED_OPERATING_SYSTEM);
    }
}
