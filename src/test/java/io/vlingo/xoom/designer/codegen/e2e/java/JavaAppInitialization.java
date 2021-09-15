// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.vlingo.xoom.designer.cli.CommandExecutionStep;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.codegen.e2e.CommandObserver;
import io.vlingo.xoom.designer.codegen.e2e.ExecutionStatus;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class JavaAppInitialization extends CommandExecutionStep {

  private final int availablePort;
  private final GenerationSettingsData generationSettings;
  private final CommandObserver observer;

  public static JavaAppInitialization run(final GenerationSettingsData generationSettings,
                                          final int availablePort) {
    final JavaAppInitialization appInitialization =
            new JavaAppInitialization(generationSettings, new CommandObserver(), availablePort);

    appInitialization.process();

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
  public void process() {
    Executors.newSingleThreadExecutor().submit(() -> super.process());
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

