// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.Terminal;

import java.io.File;
import java.util.Collections;
import java.util.List;


public abstract class CommandResolverStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        grantPermissions();
        context.withCommands(new String[]{
                Terminal.supported().initializationCommand(),
                Terminal.supported().parameter(),
                formatCommands(context)});
    }

    protected abstract String formatCommands(final TaskExecutionContext context);

    protected String resolveDirectoryChangeCommand(final String targetFolder) {
        if(Terminal.supported().isWindows()) {
            final String drive = targetFolder.substring(0, 2);
            return drive + " && cd " + targetFolder;
        }
        return "cd " + targetFolder;
    }

    protected List<File> executableFiles() {
        return Collections.emptyList();
    }

    protected void grantPermissions() {
        executableFiles().forEach(file -> Terminal.grantAllPermissions(file));
    }
}
