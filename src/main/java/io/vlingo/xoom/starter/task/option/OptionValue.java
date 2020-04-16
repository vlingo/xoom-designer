package io.vlingo.xoom.starter.task.option;

public class OptionValue {

    private final OptionName name;
    private final String value;

    public static OptionValue with(final OptionName name, final String value) {
        return new OptionValue(name, value);
    }

    private OptionValue(final OptionName name, final String value) {
        this.name = name;
        this.value = value;
    }

    public boolean hasName(final OptionName name) {
        return this.name.equals(name);
    }

    public String value() {
        return value;
    }
}