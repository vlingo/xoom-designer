package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;

import static io.vlingo.xoom.starter.template.steps.PropertiesKeys.TARGET_FOLDER;

public class WindowsCommandExecutorStep extends CommandExecutorStep {

    private static final String SUPPORTED_OPERATING_SYSTEM = "Windows";
    private static final String COMMAND_PATTERN = "cd %s && %s";

    @Override
    protected String[] prepareCommands(final TemplateGenerationContext context) {
        final String targetFolder = context.propertyOf(TARGET_FOLDER);
        final String archetypeCommand = prepareArchetypeCommand(context);
        final String commands = String.format(COMMAND_PATTERN, targetFolder, archetypeCommand);
        return new String[]{"cmd.exe", "/c", commands};
    }

    @Override
    public boolean shouldProcess() {
        return System.getProperty("os.name").contains(SUPPORTED_OPERATING_SYSTEM);
    }

}
