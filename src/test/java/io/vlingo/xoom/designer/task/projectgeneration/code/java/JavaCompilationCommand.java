// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.CommandExecutionStep;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.code.EndToEndTest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

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
    final String profileName = resolveMavenProfile();
    final Path pomPath = Paths.get(applicationPath, "pom.xml");
    final Path stagingFolderPath = Infrastructure.StagingFolder.path();
    final String directoryChangeCommand = terminal.resolveDirectoryChangeCommand(stagingFolderPath);
    return String.format("%s && %s -f %s package %s", directoryChangeCommand, terminal.mavenCommand(), pomPath, profileName);
  }

  private String resolveMavenProfile() {
    final String profileName = System.getProperty(EndToEndTest.mavenProfile);
    if(profileName == null) {
      return "";
    }
    Logger.basicLogger().info(String.format("Enabling profile %s for compilation", profileName));
    return "-P " + profileName;
  }

  @Override
  protected List<File> executableFiles() {
    return Terminal.supported().executableMavenFilesLocations();
  }

  public CommandStatus status() {
    return compilationObserver.status;
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
