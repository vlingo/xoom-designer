// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.gui;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskManager;

import java.util.List;

public class UserInterfaceManager implements TaskManager<List<String>> {

    @Override
    public void run(final List<String> args) {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        Configuration.GUI_STEPS.forEach(step -> step.process(context));
    }

}
