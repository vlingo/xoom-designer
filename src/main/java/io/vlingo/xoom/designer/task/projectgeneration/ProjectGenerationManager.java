// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskManager;

public abstract class ProjectGenerationManager<S> implements TaskManager<S> {

    protected void processSteps(final TaskExecutionContext context) {
        Configuration.PROJECT_GENERATION_STEPS.stream()
                .filter(step -> step.shouldProcess(context))
                .forEach(step -> step.process(context));
    }

}
