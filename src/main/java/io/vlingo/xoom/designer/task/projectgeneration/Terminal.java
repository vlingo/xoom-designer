// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.infrastructure.Infrastructure.ArchetypesFolder;

import java.io.File;
import java.util.Arrays;
import java.util.function.Predicate;

public enum Terminal {

    WINDOWS("cmd.exe", "/c", "mvnw.cmd", "start", osName -> osName.contains("Windows")),
    MAC_OS("sh", "-c", "./mvnw", "open", osName -> osName.toUpperCase().contains("MAC OS")),
    LINUX("sh", "-c", "./mvnw", "xdg-open", osName -> osName.contains("Linux"));

    private static Terminal ENABLED;

    private final String initializationCommand;
    private final String parameter;
    private final String mavenCommand;
    private final String browserLaunchCommand;
    private final Predicate<String> activationCondition;

    Terminal(final String initializationCommand,
             final String parameter,
             final String mavenCommand,
             final String browserLaunchCommand,
             final Predicate<String> activationCondition) {
        this.initializationCommand = initializationCommand;
        this.parameter = parameter;
        this.mavenCommand = mavenCommand;
        this.browserLaunchCommand = browserLaunchCommand;
        this.activationCondition = activationCondition;
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

    public File executableMavenFileLocation() {
        final String executableFile = mavenCommand.replaceAll("./", "");
        return ArchetypesFolder.path().resolve(executableFile).toFile();
    }

    public static void grantAllPermissions(final File file) {
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
    }

}
