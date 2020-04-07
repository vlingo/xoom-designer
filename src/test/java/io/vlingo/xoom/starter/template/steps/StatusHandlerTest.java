// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationException;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Test;

public class StatusHandlerTest {

    @Test
    public void testStatusHandlerRetrieval() {
        Assert.assertNotNull(StatusHandler.forStatus(-1));
        Assert.assertNotNull(StatusHandler.forStatus(0));
        Assert.assertNotNull(StatusHandler.forStatus(1));
        Assert.assertNotNull(StatusHandler.forStatus(2));
        Assert.assertNotNull(StatusHandler.forStatus(3));
        Assert.assertNotNull(StatusHandler.forStatus(4));
        Assert.assertNotNull(StatusHandler.forStatus(5));
    }

    @Test
    public void testSuccessHandler() {
        final StatusHandler statusHandler = StatusHandler.forStatus(0);
        statusHandler.handle(new TemplateGenerationContext());
    }

    @Test(expected = TemplateGenerationException.class)
    public void testFailureHandler() {
        final StatusHandler statusHandler = StatusHandler.forStatus(3);
        statusHandler.handle(new TemplateGenerationContext());
    }

}
