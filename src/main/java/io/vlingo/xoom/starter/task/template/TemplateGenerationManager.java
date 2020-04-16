// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

import io.vlingo.xoom.starter.task.steps.CommandExecutionStep;
import io.vlingo.xoom.starter.task.steps.LoggingStep;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.TaskManager;
import io.vlingo.xoom.starter.task.template.steps.ArchetypeCommandResolverStep;
import io.vlingo.xoom.starter.task.template.steps.PropertiesLoadStep;
import io.vlingo.xoom.starter.task.template.steps.ResourcesLocationStep;
import io.vlingo.xoom.starter.task.steps.StatusHandlingStep;

import java.util.Arrays;
import java.util.List;

public class TemplateGenerationManager implements TaskManager {

    @Override
    public void run(final List<String> args) {
        processSteps(TaskExecutionContext.withoutOptions());
    }

    private void processSteps(final TaskExecutionContext context) {
        STEPS.forEach(step -> {
            step.process(context);
        });
    }

    private static final List<TaskExecutionStep> STEPS = Arrays.asList(
            new ResourcesLocationStep(), new PropertiesLoadStep(),
            new ArchetypeCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

}
