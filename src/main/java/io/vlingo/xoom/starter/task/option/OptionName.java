package io.vlingo.xoom.starter.task.option;

public enum OptionName {

    TAG("tag"),
    CURRENT_DIRECTORY("currentDirectory"),
    PUBLISHER("publisher");

    private final String name;

    OptionName(final String name) {
        this.name = name;
    }

    public String literal() {
        return name;
    }

    public String withPreffix() {
        return "--" + name;
    }

}
