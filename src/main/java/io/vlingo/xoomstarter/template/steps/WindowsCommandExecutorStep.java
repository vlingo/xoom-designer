package io.vlingo.xoomstarter.template.steps;

public class WindowsCommandExecutorStep extends CommandExecutorStep {

    private static final String SUPPORTED_OPERATING_SYSTEM = "Windows";
    private static final String COMMAND_PATTERN = "cmd.exe /c cd %s && %s";

    @Override
    protected String commandPattern() {
        return COMMAND_PATTERN;
    }

    @Override
    public boolean shouldProcess() {
        return System.getProperty("os.name").contains(SUPPORTED_OPERATING_SYSTEM);
    }

}
