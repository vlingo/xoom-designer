package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.Archetype;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArchetypeResolverStepTest {

    private TemplateGenerationContext context;

    @Test
    public void testArchetypeStep(){
        new ArchetypeResolverStep().process(context);
        Assert.assertEquals(Archetype.DEFAULT, context.archetype());
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
    }

}
