package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.Archetype;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public class ArchetypeResolverStep implements TemplateGenerationStep  {

    @Override
    public void process(final TemplateGenerationContext context) {
        context.selectArchetype(Archetype.DEFAULT);
    }

}
