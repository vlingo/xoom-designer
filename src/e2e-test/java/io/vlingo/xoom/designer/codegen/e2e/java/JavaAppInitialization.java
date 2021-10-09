// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.designer.codegen.e2e.CommandObserver;
import io.vlingo.xoom.designer.codegen.e2e.ExecutionStatus;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.terminal.Terminal;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class JavaAppInitialization extends CommandExecutor implements TaskExecutionStep {

  private final int availablePort;
  private final GenerationSettingsData generationSettings;
  private final CommandObserver observer;

  public static JavaAppInitialization run(final GenerationSettingsData generationSettings,
                                          final int availablePort) {
    final JavaAppInitialization appInitialization =
            new JavaAppInitialization(generationSettings, new CommandObserver(), availablePort);

    appInitialization.processTask();

    return appInitialization;
  }

  private JavaAppInitialization(final GenerationSettingsData generationSettings,
                                final CommandObserver observer,
                                final int availablePort) {
    super(new ObservableCommandExecutionProcess(observer));
    this.generationSettings = generationSettings;
    this.observer = observer;
    this.availablePort = availablePort;
  }

  @Override
  public void processTask() {
    Executors.newSingleThreadExecutor().submit(() -> processTask());
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final Path projectBinariesFolder = Paths.get(generationSettings.projectDirectory, "target");
    final String directoryChangeCommand = Terminal.supported().resolveDirectoryChangeCommand(projectBinariesFolder);
    return String.format("%s && java -jar %s-%s.jar", directoryChangeCommand, generationSettings.context.artifactId, generationSettings.context.artifactVersion, availablePort);
  }

  public ExecutionStatus status() {
    return observer.status;
  }

}

