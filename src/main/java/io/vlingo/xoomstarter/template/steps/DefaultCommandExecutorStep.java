package io.vlingo.xoomstarter.template.steps;

public class DefaultCommandExecutorStep extends CommandExecutorStep {

    private static final String UNSUPPORTED_OPERATING_SYSTEM = "Windows";
    private static final String COMMAND_PATTERN = "bash -c cd %s && %s";

    @Override
    protected String commandPattern() {
        return COMMAND_PATTERN;
    }

    @Override
    public boolean shouldProcess() {
        return !System.getProperty("os.name").contains(UNSUPPORTED_OPERATING_SYSTEM);
    }
}
