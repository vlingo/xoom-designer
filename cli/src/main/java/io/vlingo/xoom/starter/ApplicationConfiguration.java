package io.vlingo.xoom.starter;

public enum ApplicationConfiguration {
    USER_INTERFACE("ui");

    private final String key;

    ApplicationConfiguration(final String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
