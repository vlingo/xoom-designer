package io.vlingo.xoom.starter.task;

public class Option {

    private final String name;
    private final String defaultValue;
    private final boolean required;

    public Option(final String name,
                  final String defaultValue,
                  final boolean required) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public static final Option of(final String name, final String defaultValue) {
        return new Option(name, defaultValue, false);
    }

    public static final Option required(final String name) {
        return new Option(name, null, true);
    }

}
