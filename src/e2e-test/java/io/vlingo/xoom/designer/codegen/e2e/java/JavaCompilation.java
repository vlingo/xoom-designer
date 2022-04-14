// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.codegen.e2e.CommandObserver;
import io.vlingo.xoom.designer.codegen.e2e.ExecutionStatus;
import io.vlingo.xoom.designer.infrastructure.StagingFolder;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.terminal.Terminal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavaCompilation extends CommandExecutor {

  private final String applicationPath;
  private final CommandObserver commandObserver;
  private static final String mavenProfile = "e2e-tests-maven-profile";

  public static JavaCompilation run(final String applicationPath) {
    final JavaCompilation compilation = new JavaCompilation(applicationPath, new CommandObserver());
    compilation.execute();
    return compilation;
  }

  private JavaCompilation(final String applicationPath, final CommandObserver commandObserver) {
    super(new ObservableCommandExecutionProcess(commandObserver));
    this.applicationPath = applicationPath;
    this.commandObserver = commandObserver;
  }

  @Override
  protected String formatCommands() {
    final Terminal terminal = Terminal.supported();
    final String profileName = resolveMavenProfile();
    final Path pomPath = Paths.get(applicationPath, "pom.xml");
    final Path stagingFolderPath = StagingFolder.path();
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

  public ExecutionStatus status() {
    return commandObserver.status;
  }

}
