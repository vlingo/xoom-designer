// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JavaCompilationCommand extends CommandExecutionStep {

  private final String applicationPath;
  private final CompilationObserver compilationObserver;

  public static JavaCompilationCommand at(final String applicationPath) {
    return new JavaCompilationCommand(applicationPath, new CompilationObserver());
  }

  private JavaCompilationCommand(final String applicationPath, final CompilationObserver compilationObserver) {
    super(new ObservableCommandExecutionProcess(compilationObserver));
    this.applicationPath = applicationPath;
    this.compilationObserver = compilationObserver;
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final Terminal terminal = Terminal.supported();
    final Path pomPath = Paths.get(applicationPath, "pom.xml");
    final Path designerPath = Paths.get(System.getProperty("user.dir"));
    final String directoryChangeCommand = terminal.resolveDirectoryChangeCommand(designerPath);
    return String.format("%s && %s -f %s package", directoryChangeCommand, terminal.mavenCommand(), pomPath);
  }

  public CommandStatus status() {
    return compilationObserver.status;
  }

  @Override
  protected List<File> executableFiles() {
    return Terminal.supported().executableMavenFilesLocations();
  }

  public static class CompilationObserver implements ObservableCommandExecutionProcess.CommandExecutionObserver {

    public CommandStatus status;

    public CompilationObserver() {
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
