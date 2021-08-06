// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionException;
import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SchemaPushStep extends CommandExecutionStep {

  public SchemaPushStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final Terminal terminal =  Terminal.supported();

    final Path projectPath =
            Paths.get(context.targetFolder());

    final String directoryChangeCommand =
            terminal.resolveDirectoryChangeCommand(projectPath);

    return String.format("%s && %s io.vlingo.xoom:xoom-build-plugins:push-schema@push",
            directoryChangeCommand, terminal.mavenCommand());
  }

  @Override
  protected void logPreliminaryMessage(final TaskExecutionContext context) {
    context.logger().info("[INFO] ------------------------------------------------------------------------");
    context.logger().info("[INFO] PUSHING SCHEMAS MAY REQUIRE A FEW SECONDS");
    context.logger().info("[INFO] ------------------------------------------------------------------------");
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return context.codeGenerationParametersOf(Label.AGGREGATE)
            .filter(aggregate -> aggregate.hasAny(Label.EXCHANGE))
            .flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .anyMatch(exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer());
  }

  @Override
  protected CommandExecutionException resolveCommandExecutionException(final CommandExecutionException exception) {
    return new SchemaPushException(exception);
  }

}
