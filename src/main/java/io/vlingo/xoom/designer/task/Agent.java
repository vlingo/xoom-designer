package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.designer.ArgumentNotFoundException;
import io.vlingo.xoom.designer.ArgumentRetriever;

import java.util.List;

public enum Agent {

    WEB,
    TERMINAL;

    public boolean isTerminal() {
        return this.equals(TERMINAL);
    }

}
