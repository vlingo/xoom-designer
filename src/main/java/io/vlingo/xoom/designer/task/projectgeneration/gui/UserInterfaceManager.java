// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskManager;

import java.util.List;

public class UserInterfaceManager implements TaskManager<List<String>> {

    @Override
    public void run(final List<String> args) {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        Configuration.GUI_STEPS.forEach(step -> step.process(context));
    }

}
