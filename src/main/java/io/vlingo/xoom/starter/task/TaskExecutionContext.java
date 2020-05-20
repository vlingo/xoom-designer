// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.option.OptionName;
import io.vlingo.xoom.starter.task.option.OptionValue;
import io.vlingo.xoom.starter.task.template.steps.DeploymentType;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

import static io.vlingo.xoom.starter.task.Property.*;

public class TaskExecutionContext {

    private Process process;
    private String[] commands;
    private Properties properties;
    private final List<String> args = new ArrayList<>();
    private final List<OptionValue> optionValues = new ArrayList<>();
    private final Map<String, String> configuration = new HashMap<>();
    private final List<Content> contents = new ArrayList<>();

    public static TaskExecutionContext withOptions(final List<OptionValue> optionValues) {
        return new TaskExecutionContext(optionValues);
    }

    public static TaskExecutionContext withArgs(final List<String> args) {
        final TaskExecutionContext context = new TaskExecutionContext();
        context.addArgs(args);
        return context;
    }

    private TaskExecutionContext() {
        this(Collections.emptyList());
    }

    private TaskExecutionContext(final List<OptionValue> optionValues) {
        this.optionValues.addAll(optionValues);
    }

    public static TaskExecutionContext withoutOptions() {
        return new TaskExecutionContext();
    }

    public void followProcess(final Process process) {
        this.process = process;
    }

    public void addContent(final Object subject, final File file, final String text) {
        this.contents.add(Content.with(subject, file, text));
    }

    public void onConfiguration(final Map<String, String> configurations) {
        this.configuration.putAll(configurations);
    }

    public void onProperties(final Properties properties) {
        this.properties = properties;
    }

    public void withCommands(final String[] commands) {
        this.commands = commands;
    }

    public void addArgs(final List<String> args) {
        this.args.addAll(args);
    }

    public Process process() {
        return process;
    }

    public Properties properties() {
        return properties;
    }

    public String configurationOf(final String key) {
        return this.configuration.get(key);
    }

    public <T> T propertyOf(final Property property) {
        return (T) propertyOf(property, value -> value);
    }

    public <T> T propertyOf(final Property property, final Function<String, T> mapper) {
        final String value = properties.getProperty(property.literal());
        return (T) mapper.apply(value);
    }

    public String optionValueOf(final OptionName optionName) {
        return optionValues.stream()
                .filter(optionValue -> optionValue.hasName(optionName))
                .map(optionValue -> optionValue.value()).findFirst().get();
    }

    public DeploymentType deploymentType() {
        return DeploymentType.valueOf(propertyOf(DEPLOYMENT));
    }

    public String[] commands() {
        return commands;
    }

    public List<String> args() {
        return args;
    }

    public List<Content> contents() {
        return contents;
    }

    public String projectPath() {
        final String artifactId = propertyOf(ARTIFACT_ID);
        final String targetFolder = propertyOf(TARGET_FOLDER);
        return Paths.get(targetFolder, artifactId).toString();
    }

    public boolean hasProperty(final Property property) {
        return this.propertyOf(property) != null && !this.<String>propertyOf(property).trim().isEmpty();
    }

}
