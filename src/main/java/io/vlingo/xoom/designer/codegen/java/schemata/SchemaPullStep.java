// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.SchemataSettings;
import io.vlingo.xoom.designer.codegen.java.exchange.ExchangeRole;
import io.vlingo.xoom.terminal.CommandExecutionException;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.Terminal;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SchemaPullStep implements TaskExecutionStep {

  private final CommandExecutionProcess commandExecutionProcess;

  public SchemaPullStep(final CommandExecutionProcess commandExecutionProcess) {
    this.commandExecutionProcess = commandExecutionProcess;
  }

  @Override
  public void processTaskWith(final TaskExecutionContext context) {
    new CommandExecutor(commandExecutionProcess) {
      @Override
      protected String formatCommands() {
        final Terminal terminal = Terminal.supported();

        final Path projectPath =
                Paths.get(context.targetFolder());

        final String directoryChangeCommand =
                terminal.resolveDirectoryChangeCommand(projectPath);

        final SchemataSettings schemataSettings =
                context.codeGenerationParameters().retrieveObject(Label.SCHEMATA_SETTINGS);

        final String schemataServiceProfile =
                SchemataServiceProfileResolver.resolveSchemataProfile(schemataSettings);

        return String.format("%s && %s io.vlingo.xoom:xoom-build-plugins:pull-schema@pull %s",
                directoryChangeCommand, terminal.mavenCommand(), schemataServiceProfile);
      }

      @Override
      protected void logPreliminaryMessage() {
        logger().info("[INFO] ------------------------------------------------------------------------");
        logger().info("[INFO] PULLING SCHEMAS MAY REQUIRE A FEW SECONDS");
        logger().info("[INFO] ------------------------------------------------------------------------");
      }

      @Override
      protected CommandExecutionException resolveCommandExecutionException(final CommandExecutionException exception) {
        return new SchemaPullException(exception);
      }
    }.execute();
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return context.codeGenerationParametersOf(Label.AGGREGATE)
            .filter(aggregate -> aggregate.hasAny(Label.EXCHANGE))
            .flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .anyMatch(exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isConsumer());
  }

}
