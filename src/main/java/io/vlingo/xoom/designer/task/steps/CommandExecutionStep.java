// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutionException;

import java.io.IOException;

public class CommandExecutionStep implements TaskExecutionStep {

    public void process(final TaskExecutionContext context) {
        try {
            context.followProcess(
                Runtime.getRuntime().exec(context.commands())
            );
        } catch (final IOException e) {
            e.printStackTrace();
            throw new TaskExecutionException(e);
        }
    }
}
