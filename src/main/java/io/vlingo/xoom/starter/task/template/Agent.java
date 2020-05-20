package io.vlingo.xoom.starter.task.template;

import io.vlingo.xoom.starter.ArgumentNotFoundException;
import io.vlingo.xoom.starter.ArgumentRetriever;

import java.util.List;

public enum Agent {

    WEB,
    TERMINAL;

    public static Agent findAgent(final List<String> args) {
        try {
            return Agent.valueOf(ArgumentRetriever.retrieve(argumentKey(), args));
        } catch(final ArgumentNotFoundException exception) {
            return TERMINAL;
        }
    }

    public static String argumentKey() {
        return "AGENT";
    }

    public boolean isTerminal() {
        return this.equals(TERMINAL);
    }

}
