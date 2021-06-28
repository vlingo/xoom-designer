package io.vlingo.xoom.designer.task;

public enum Agent {

    WEB,
    TERMINAL;

    public boolean isTerminal() {
        return this.equals(TERMINAL);
    }

}
