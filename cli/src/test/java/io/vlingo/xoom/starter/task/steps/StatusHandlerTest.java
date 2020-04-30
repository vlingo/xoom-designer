// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusHandlerTest {

    @Test
    public void testStatusHandlerRetrieval() {
        Assertions.assertNotNull(StatusHandler.forStatus(-1));
        Assertions.assertNotNull(StatusHandler.forStatus(0));
        Assertions.assertNotNull(StatusHandler.forStatus(1));
        Assertions.assertNotNull(StatusHandler.forStatus(2));
        Assertions.assertNotNull(StatusHandler.forStatus(3));
        Assertions.assertNotNull(StatusHandler.forStatus(4));
        Assertions.assertNotNull(StatusHandler.forStatus(5));
    }

    @Test
    public void testSuccessHandler() {
        final StatusHandler statusHandler = StatusHandler.forStatus(0);
        statusHandler.handle(TaskExecutionContext.withoutOptions());
    }

    @Test
    public void testFailureHandler() {
        final StatusHandler statusHandler = StatusHandler.forStatus(3);
        Assertions.assertThrows(TemplateGenerationException.class, () -> {
            statusHandler.handle(TaskExecutionContext.withoutOptions());
        });
    }

}
