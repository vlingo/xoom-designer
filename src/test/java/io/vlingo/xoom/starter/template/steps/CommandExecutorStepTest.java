package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.Archetype;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class CommandExecutorStepTest {

    private TemplateGenerationContext context;

    private static final String EXPECTED_COMMAND = "cd E:\\projects && " +
            "mvn archetype:generate -B " +
            "-DarchetypeGroupId=io.vlingo " +
            "-DarchetypeArtifactId=vlingo-xoom-basic-archetype " +
            "-DarchetypeVersion=1.0 " +
            "-DgroupId=io.vlingo " +
            "-DartifactId=starter-example " +
            "-Dversion=1.0 " +
            "-Dpackage=io.vlingo.starterexample";

    @Test
    public void testDefaultCommandPreparation () {
        final CommandExecutorStep step = new DefaultCommandExecutorStep();
        final String[] commands = step.prepareCommands(context);
        Assert.assertEquals("sh", commands[0]);
        Assert.assertEquals("-c", commands[1]);
        Assert.assertEquals(EXPECTED_COMMAND, commands[2]);
    }

    @Test
    public void testWindowsCommandPreparation () {
        final CommandExecutorStep step = new WindowsCommandExecutorStep();
        final String[] commands = step.prepareCommands(context);
        Assert.assertEquals("cmd.exe", commands[0]);
        Assert.assertEquals("/c", commands[1]);
        Assert.assertEquals(EXPECTED_COMMAND, commands[2]);
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
        this.context.selectArchetype(Archetype.DEFAULT);
        this.context.onProperties(loadProperties());
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        properties.put(PropertiesKeys.VERSION.literal(), "1.0");
        properties.put(PropertiesKeys.GROUP_ID.literal(), "io.vlingo");
        properties.put(PropertiesKeys.ARTIFACT_ID.literal(), "starter-example");
        properties.put(PropertiesKeys.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(PropertiesKeys.TARGET_FOLDER.literal(), "E:\\projects");
        return properties;
    }

}
