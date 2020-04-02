package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.Archetype;
import io.vlingo.xoomstarter.template.TemplateGenerationContext;

public class ArchetypeResolverStep implements TemplateGenerationStep  {

    private static final String ARCHETYPE_CMD = "mvn archetype:generate -B " +
            "-DarchetypeGroupId=org.apache.maven.archetypes " +
            "-DarchetypeArtifactId=maven-archetype-quickstart " +
            "-DarchetypeVersion=1.1 " +
            "-DgroupId=io.vlingo " +
            "-DartifactId=starter-example " +
            "-Dversion=1.0-SNAPSHOT " +
            "-Dpackage=io.vlingo.starterexample";

    @Override
    public void process(final TemplateGenerationContext context) {
        context.selectArchetype(Archetype.DEFAULT);
    }
}
