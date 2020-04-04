package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.archetype.ArchetypeProperties;
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
        Assert.assertEquals("1.0", context.propertyOf(ArchetypeProperties.VERSION));
        Assert.assertEquals("com.company", context.propertyOf(ArchetypeProperties.GROUP_ID));
        Assert.assertEquals("xoom-application", context.propertyOf(ArchetypeProperties.ARTIFACT_ID));
        Assert.assertEquals("com.company.business", context.propertyOf(ArchetypeProperties.PACKAGE));
        Assert.assertEquals("k8s", context.propertyOf(ArchetypeProperties.DEPLOYMENT));
        Assert.assertNotNull(context.propertyOf(ArchetypeProperties.TARGET_FOLDER));
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
    }

}
