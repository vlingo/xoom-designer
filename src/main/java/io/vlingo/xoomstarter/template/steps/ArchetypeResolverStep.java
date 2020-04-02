package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.Archetype;
import io.vlingo.xoomstarter.template.TemplateGenerationContext;

public class ArchetypeResolverStep implements TemplateGenerationStep  {

    @Override
    public void process(final TemplateGenerationContext context) {
        context.selectArchetype(Archetype.DEFAULT);
    }

}
