// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;

public abstract class CommandResolverStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.withCommands(new String[]{
                Terminal.active().initializationCommand(),
                Terminal.active().parameter(),
                formatCommands(context)});
    }

    protected abstract String formatCommands(final TaskExecutionContext context);
}
