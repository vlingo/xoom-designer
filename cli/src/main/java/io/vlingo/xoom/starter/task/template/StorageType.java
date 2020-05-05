package io.vlingo.xoom.starter.task.template;

public enum StorageType {

    JOURNAL("JOURNAL"),
    OBJECT_STORE("OBJECT_STORE"),
    STATE_STORE("STATE_STORE");

    private final String key;

    StorageType(String key) {
        this.key = key;
    }

    public String literal() {
        return key;
    }

    public static StorageType of(final String storage) {
        return valueOf(storage.toUpperCase());
    }

}
