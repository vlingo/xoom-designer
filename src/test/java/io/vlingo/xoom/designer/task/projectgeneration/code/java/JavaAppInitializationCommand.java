// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class JavaAppInitializationCommand extends CommandExecutionStep {

  private final int availablePort;
  private final GenerationSettingsData generationSettings;
  private final JavaCompilationCommand.CompilationObserver compilationObserver;

  public static JavaAppInitializationCommand from(final GenerationSettingsData generationSettings,
                                                  final int availablePort) {
    return new JavaAppInitializationCommand(generationSettings, new JavaCompilationCommand.CompilationObserver(), availablePort);
  }

  private JavaAppInitializationCommand(final GenerationSettingsData generationSettings,
                                       final JavaCompilationCommand.CompilationObserver compilationObserver,
                                       final int availablePort) {
    super(new ObservableCommandExecutionProcess(compilationObserver));
    this.generationSettings = generationSettings;
    this.compilationObserver = compilationObserver;
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
    return String.format("%s && java -jar %s-%s.jar -Dport=%s", directoryChangeCommand, generationSettings.context.artifactId, generationSettings.context.artifactVersion, availablePort);
  }

  public CommandStatus status() {
    return compilationObserver.status;
  }

  public static class InitializationObserver implements ObservableCommandExecutionProcess.CommandExecutionObserver {

    public CommandStatus status;

    public InitializationObserver() {
      status = CommandStatus.IN_PROGRESS;
    }

    @Override
    public void onSuccess() {
      status = CommandStatus.SUCCEEDED;
    }

    @Override
    public void onFailure() {
      status = CommandStatus.FAILED;
    }

  }

}

