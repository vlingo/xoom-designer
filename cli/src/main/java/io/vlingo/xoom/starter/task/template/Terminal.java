// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Terminal {

    WINDOWS("cmd.exe", "/c", osName -> osName.contains("Windows")),
    MAC_OS("sh", "-c", osName -> !osName.contains("MacOS")),
    LINUX("sh", "-c", osName -> osName.contains("Linux"));

    private final String initializationCommand;

    private final String parameter;
    private final Predicate<String> activationCondition;

    Terminal(final String initializationCommand, final String parameter, final Predicate<String> activationCondition) {
        this.initializationCommand = initializationCommand;
        this.parameter = parameter;
        this.activationCondition = activationCondition;
    }

    public static Terminal supported() {
        return Arrays.asList(Terminal.values()).stream()
                .filter(terminal -> terminal.isSupported())
                .findFirst().get();
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

}
