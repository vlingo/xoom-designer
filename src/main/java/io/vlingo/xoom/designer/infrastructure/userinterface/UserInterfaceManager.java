// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.cli.Task;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskManager;

import java.util.List;

import static io.vlingo.xoom.designer.cli.Agent.TERMINAL;

public class UserInterfaceManager implements TaskManager<List<String>> {

    @Override
    public void run(final List<String> args) {
        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .withOptions(Task.GRAPHICAL_USER_INTERFACE.findOptionValues(args));

        Configuration.GUI_STEPS.stream().filter(step -> step.shouldProcess(context))
                .forEach(step -> step.process(context));
    }

}
