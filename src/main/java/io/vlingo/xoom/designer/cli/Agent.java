package io.vlingo.xoom.designer.cli;

public enum Agent {

    WEB,
    TERMINAL;

    public boolean isTerminal() {
        return this.equals(TERMINAL);
    }

}
