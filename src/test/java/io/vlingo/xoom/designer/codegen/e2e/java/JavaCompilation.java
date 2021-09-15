// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.cli.CommandExecutionStep;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.codegen.e2e.CommandStatus;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavaCompilation extends CommandExecutionStep {

  private final String applicationPath;
  private final CompilationObserver compilationObserver;
  private static final String mavenProfile = "e2e-tests-maven-profile";

  public static JavaCompilation run(final String applicationPath) {
    final JavaCompilation compilation = new JavaCompilation(applicationPath, new CompilationObserver());
    compilation.process();
    return compilation;
  }

  private JavaCompilation(final String applicationPath, final CompilationObserver compilationObserver) {
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
    final String profileName = System.getProperty(mavenProfile);
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
