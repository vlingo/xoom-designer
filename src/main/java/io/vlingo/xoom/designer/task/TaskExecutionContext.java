// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.option.OptionName;
import io.vlingo.xoom.designer.task.option.OptionValue;
import io.vlingo.xoom.designer.task.projectgeneration.steps.DeploymentType;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import static io.vlingo.xoom.turbo.codegen.parameter.Label.DEPLOYMENT;
import static io.vlingo.xoom.turbo.codegen.parameter.Label.TARGET_FOLDER;

public class TaskExecutionContext {

    private Properties properties;
    private final CodeGenerationParameters parameters;
    private final List<String> args = new ArrayList<>();
    private final List<OptionValue> optionValues = new ArrayList<>();
    private final Agent agent;

    public static TaskExecutionContext executedFrom(final Agent agent) {
        return new TaskExecutionContext(agent);
    }

    public static TaskExecutionContext withoutOptions() {
        return new TaskExecutionContext(Agent.TERMINAL);
    }

    private TaskExecutionContext(final Agent agent) {
        this.agent = agent;
        this.parameters = CodeGenerationParameters.empty();
    }

    public TaskExecutionContext withOptions(final List<OptionValue> optionValues) {
        this.optionValues.addAll(optionValues);
        return this;
    }

    public TaskExecutionContext withArgs(final List<String> args) {
        this.addArgs(args);
        return this;
    }

    public TaskExecutionContext with(final CodeGenerationParameters parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    public TaskExecutionContext onProperties(final Properties properties) {
        this.properties = properties;
        return this;
    }

    public void addArgs(final List<String> args) {
        this.args.addAll(args);
    }

    public Properties properties() {
        return properties;
    }

    public Agent agent() {
        return agent;
    }

    public <T> T propertyOf(final Property property) {
        return (T) propertyOf(property, value -> value);
    }

    public <T> T propertyOf(final Property property, final Function<String, T> mapper) {
        return this.propertyOf(property.literal(), mapper);
    }

    public <T> T propertyOf(final String propertyValue, final Function<String, T> mapper) {
        final String value = properties.getProperty(propertyValue);
        return (T) mapper.apply(value);
    }

    public boolean hasProperty(final Property property) {
        return this.propertyOf(property) != null && !this.<String>propertyOf(property).trim().isEmpty();
    }

    public String optionValueOf(final OptionName optionName) {
        return optionValues.stream()
                .filter(optionValue -> optionValue.hasName(optionName))
                .map(optionValue -> optionValue.value()).findFirst().get();
    }

    public boolean hasOption(final OptionName optionName) {
        return optionValues.stream().anyMatch(optionValue -> optionValue.hasName(optionName));
    }

    public List<String> args() {
        return args;
    }

    public CodeGenerationParameters codeGenerationParameters() {
        return parameters;
    }

    public DeploymentType deploymentType() {
        return DeploymentType.valueOf(parameters.retrieveValue(DEPLOYMENT));
    }

    public String projectPath() {
        return Paths.get(parameters.retrieveValue(TARGET_FOLDER)).toString();
    }
}
