// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskManager;

import java.util.List;

public class TemplateGenerationManager implements TaskManager {

    @Override
    public void run(final List<String> args) {
        processSteps(TaskExecutionContext.withArgs(args));
    }

    private void processSteps(final TaskExecutionContext context) {
        Configuration.TEMPLATE_GENERATION_STEPS.stream()
                .filter(step -> step.shouldProcess(context))
                .forEach(step -> step.process(context));
    }

}
