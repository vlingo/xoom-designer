// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.terminal;

import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.StagingFolder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Terminal {

  WINDOWS("cmd.exe", "/c", "mvnw.cmd", "start",
          osName -> osName.contains("Windows"),
          targetFolder -> targetFolder.substring(0, 2) + " && cd " + targetFolder),

  MAC_OS("sh", "-c", "./mvnw", "open",
          osName -> osName.toUpperCase().contains("MAC OS"),
          targetFolder -> "cd " + targetFolder),

  LINUX("sh", "-c", "./mvnw", "xdg-open",
          osName -> osName.contains("Linux"),
          targetFolder -> "cd " + targetFolder);

  private static Terminal ENABLED;

  private final String initializationCommand;
  private final String parameter;
  private final String mavenCommand;
  private final String browserLaunchCommand;
  private final Predicate<String> activationCondition;
  private final Function<String, String> directoryChangeCommandResolver;

  Terminal(final String initializationCommand,
           final String parameter,
           final String mavenCommand,
           final String browserLaunchCommand,
           final Predicate<String> activationCondition,
           final Function<String, String> directoryChangeCommandResolver) {
    this.initializationCommand = initializationCommand;
    this.parameter = parameter;
    this.mavenCommand = mavenCommand;
    this.browserLaunchCommand = browserLaunchCommand;
    this.activationCondition = activationCondition;
    this.directoryChangeCommandResolver = directoryChangeCommandResolver;
  }

  public static Terminal supported() {
    if(ENABLED != null) {
      return ENABLED;
    }
    return Arrays.asList(Terminal.values()).stream()
            .filter(terminal -> terminal.isSupported())
            .findFirst().get();
  }

  public static void enable(final Terminal terminal) {
    ENABLED = terminal;
  }

  public static void disable() {
    ENABLED = null;
  }

  public String initializationCommand() {
    return initializationCommand;
  }

  public String parameter() {
    return parameter;
  }

  private boolean isSupported() {
    return activationCondition.test(System.getProperty("os.name"));
  }

  public boolean isWindows() {
    return this.equals(WINDOWS);
  }

  public String mavenCommand() {
    return mavenCommand;
  }

  public String browserLaunchCommand() {
    return browserLaunchCommand;
  }

  public String[] prepareCommand(final String command) {
    return new String[]{initializationCommand(), parameter(), command};
  }

  public List<File> executableMavenFilesLocations() {
    final String executableFile = mavenCommand.replaceAll("./", "");
    final Path homeFolderPath = Paths.get(HomeDirectory.fromEnvironment().path);
    final Path stagingFolderPath = StagingFolder.path();
    return Arrays.asList(
            homeFolderPath.resolve(executableFile).toFile(),
            stagingFolderPath.resolve(executableFile).toFile());
  }

  public String resolveDirectoryChangeCommand(final Path path) {
    return resolveDirectoryChangeCommand(path.toString());
  }

  public String resolveDirectoryChangeCommand(final String targetFolder) {
    return directoryChangeCommandResolver.apply(targetFolder);
  }

  public static void grantAllPermissions(final File file) {
    file.setReadable(true);
    file.setWritable(true);
    file.setExecutable(true);
  }

  public String pathSeparator() {
    if(equals(WINDOWS)) {
      return "\\";
    }
    return "/";
  }
}
