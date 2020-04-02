package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.Archetype;
import io.vlingo.xoomstarter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static io.vlingo.xoomstarter.template.steps.PropertiesKeys.*;

public class CommandExecutorStepTest {

    private TemplateGenerationContext context;

    private static final String EXPECTED_COMMAND_ON_OTHER_OS = "bash -c cd E:\\projects && " +
            "mvn archetype:generate -B " +
            "-DarchetypeGroupId=org.apache.maven.archetypes " +
            "-DarchetypeArtifactId=maven-archetype-quickstart " +
            "-DarchetypeVersion=1.1 " +
            "-DgroupId=io.vlingo " +
            "-DartifactId=starter-example " +
            "-Dversion=1.0 " +
            "-Dpackage=io.vlingo.starterexample";

    private static final String EXPECTED_COMMAND_ON_WINDOWS = "cmd.exe /c cd E:\\projects && " +
            "mvn archetype:generate -B " +
            "-DarchetypeGroupId=org.apache.maven.archetypes " +
            "-DarchetypeArtifactId=maven-archetype-quickstart " +
            "-DarchetypeVersion=1.1 " +
            "-DgroupId=io.vlingo " +
            "-DartifactId=starter-example " +
            "-Dversion=1.0 " +
            "-Dpackage=io.vlingo.starterexample";

    @Test
    public void testDefaultCommandPreparation () {
        final CommandExecutorStep step = new DefaultCommandExecutorStep();
        final String command = step.prepareCommand(context);
        Assert.assertEquals(EXPECTED_COMMAND_ON_OTHER_OS, command);
    }

    @Test
    public void testWindowsCommandPreparation () {
        final CommandExecutorStep step = new WindowsCommandExecutorStep();
        final String command = step.prepareCommand(context);
        Assert.assertEquals(EXPECTED_COMMAND_ON_WINDOWS, command);
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
        this.context.selectArchetype(Archetype.DEFAULT);
        this.context.onProperties(loadProperties());
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        properties.put(VERSION.literal(), "1.0");
        properties.put(GROUP_ID.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "starter-example");
        properties.put(PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(TARGET_FOLDER.literal(), "E:\\projects");
        return properties;
    }

}
