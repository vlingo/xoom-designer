// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.steps;

import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

public class StatusHandlingStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            final Integer status = context.process().waitFor();
            StatusHandler.forStatus(status).handle(context);
        } catch (final InterruptedException e) {
            System.out.println(e.getMessage());
            throw new ProjectGenerationException(e);
        }
    }

}
