package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import io.vlingo.xoomstarter.template.TemplateGenerationException;
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
