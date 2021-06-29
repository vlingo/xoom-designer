// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.DeploymentType;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.DEPLOYMENT;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.TARGET_FOLDER;

public class TaskExecutionContext {

  public final String executionId;
  private Properties properties;
  private final CodeGenerationParameters parameters;
  private final List<String> args = new ArrayList<>();
  private final List<OptionValue> optionValues = new ArrayList<>();
  private final Map<TaskOutput, Object> taskOutput = new HashMap<>();
  private final Agent agent;

  public static TaskExecutionContext executedFrom(final Agent agent) {
    return new TaskExecutionContext(agent);
  }

  public static TaskExecutionContext withoutOptions() {
    return new TaskExecutionContext(Agent.TERMINAL);
  }

  private TaskExecutionContext(final Agent agent) {
    this.agent = agent;
    this.executionId = UUID.randomUUID().toString();
    this.parameters = CodeGenerationParameters.empty();
  }

  public static TaskExecutionContext empty() {
    return withoutOptions();
  }

  public TaskExecutionContext withOptions(final List<OptionValue> optionValues) {
    this.optionValues.addAll(optionValues);
    return this;
  }

  public TaskExecutionContext withArgs(final List<String> args) {
    this.addArgs(args);
    return this;
  }

  public static TaskExecutionContext withOutput(final TaskOutput taskOutput,
                                                final Object output) {
    return withoutOptions().addOutput(taskOutput, output);
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

  public TaskExecutionContext addOutput(final TaskOutput taskOutput, final Object content) {
    this.taskOutput.put(taskOutput, content);
    return this;
  }

  public <T> T retrieveOutput(final TaskOutput taskOutput) {
    if(!this.taskOutput.containsKey(taskOutput)) {
      return null;
    }
    return (T) this.taskOutput.get(taskOutput);
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

  public <T> T codeGenerationParameterOf(final Label label) {
    return (T) parameters.retrieveValue(label);
  }

  public Stream<CodeGenerationParameter> codeGenerationParametersOf(final Label label) {
    return parameters.retrieveAll(label);
  }

  public CodeGenerationParameters codeGenerationParameters() {
    return parameters;
  }

  public DeploymentType deploymentType() {
    return DeploymentType.valueOf(parameters.retrieveValue(DEPLOYMENT));
  }

  public String targetFolder() {
    return Paths.get(parameters.retrieveValue(TARGET_FOLDER)).toString();
  }

  public Properties properties() {
    return properties;
  }

  public List<String> args() {
    return args;
  }

  public Agent agent() {
    return agent;
  }
}
