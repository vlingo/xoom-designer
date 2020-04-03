package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertiesLoadStepTest {

    private TemplateGenerationContext context;

    @Test
    public void testPropertiesLoad(){
        new PropertiesLoadStep().process(context);
        Assert.assertNotNull(context.properties());
        Assert.assertEquals("1.0", context.propertyOf(PropertiesKeys.VERSION));
        Assert.assertEquals("io.vlingo", context.propertyOf(PropertiesKeys.GROUP_ID));
        Assert.assertEquals("xoom-starter-tests", context.propertyOf(PropertiesKeys.ARTIFACT_ID));
        Assert.assertEquals("io.vlingo.starterexample", context.propertyOf(PropertiesKeys.PACKAGE));
        Assert.assertEquals("k8s", context.propertyOf(PropertiesKeys.DEPLOYMENT));
        Assert.assertNotNull(context.propertyOf(PropertiesKeys.TARGET_FOLDER));
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
    }

}
