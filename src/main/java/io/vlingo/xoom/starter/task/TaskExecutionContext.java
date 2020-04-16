// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.option.OptionName;
import io.vlingo.xoom.starter.task.option.OptionValue;

import java.util.*;

public class TaskExecutionContext {

    private Process process;
    private String[] commands;
    private Properties properties;
    private final List<OptionValue> optionValues = new ArrayList<>();

    private TaskExecutionContext() {
        this(Collections.emptyList());
    }

    private TaskExecutionContext(final List<OptionValue> optionValues) {
        this.optionValues.addAll(optionValues);
    }

    public static TaskExecutionContext withOptions(final List<OptionValue> optionValues) {
        return new TaskExecutionContext(optionValues);
    }

    public static TaskExecutionContext withoutOptions() {
        return new TaskExecutionContext();
    }

    public void followProcess(final Process process) {
        this.process = process;
    }

    public void onProperties(final Properties properties) {
        this.properties = properties;
    }

    public void withCommands(final String[] commands) {
        this.commands = commands;
    }

    public Process process() {
        return process;
    }

    public Properties properties() {
        return properties;
    }

    public String propertyOf(final Property key) {
        return properties != null ? properties.getProperty(key.literal()) : "";
    }

    public String optionValueOf(final OptionName optionName) {
        return optionValues.stream()
                .filter(optionValue -> optionValue.hasName(optionName))
                .map(optionValue -> optionValue.value()).findFirst().get();
    }

    public String[] commands() {
        return commands;
    }
}
