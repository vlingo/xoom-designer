package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.vlingo.xoomstarter.template.steps.PropertiesKeys.*;

public class PropertiesLoadStepTest {

    private TemplateGenerationContext context;

    @Test
    public void testPropertiesLoad(){
        new PropertiesLoadStep().process(context);
        Assert.assertNotNull(context.properties());
        Assert.assertEquals("1.0", context.propertyOf(VERSION));
        Assert.assertEquals("io.vlingo", context.propertyOf(GROUP_ID));
        Assert.assertEquals("xoom-starter-tests", context.propertyOf(ARTIFACT_ID));
        Assert.assertEquals("io.vlingo.starterexample", context.propertyOf(PACKAGE));
        Assert.assertEquals("k8s", context.propertyOf(DEPLOYMENT));
        Assert.assertNotNull(context.propertyOf(TARGET_FOLDER));
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
    }

}
