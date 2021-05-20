package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.designer.ArgumentNotFoundException;
import io.vlingo.xoom.designer.ArgumentRetriever;

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
