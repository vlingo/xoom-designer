package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.ArgumentNotFoundException;
import io.vlingo.xoom.starter.ArgumentRetriever;

import java.util.List;

public enum Agent {

    WEB,
    TERMINAL;

    public static Agent findAgent(final List<String> args) {
        try {
            return Agent.valueOf(ArgumentRetriever.retrieve("AGENT", args));
        } catch(final ArgumentNotFoundException exception) {
            return TERMINAL;
        }
    }

    public boolean isTerminal() {
        return this.equals(TERMINAL);
    }

}
