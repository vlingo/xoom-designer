package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.archetype.ArchetypeProperties;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static java.io.File.separator;

public class CommandExecutorStepTest {

    private TemplateGenerationContext context;
    private CommandExecutorStep commandExecutorStep;

    private static final String EXPECTED_BASIC_ARCHETYPE_COMMAND =
            "cd "+ System.getProperty("user.dir") +
            separator + "resources" + separator + "archetypes" +
            separator + "basic-archetype && " +
            "mvn clean install && cd E:\\projects && " +
            "mvn archetype:generate -B -DarchetypeGroupId=io.vlingo " +
            "-DarchetypeArtifactId=vlingo-xoom-basic-archetype " +
            "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
            "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
            "-DvlingoXoomServerVersion=1.2.9 ";

    private static final String EXPECTED_K8S_ARCHETYPE_COMMAND =
            "cd "+ System.getProperty("user.dir") +
            separator + "resources" + separator + "archetypes" +
            separator + "kubernetes-archetype && " +
            "mvn clean install && cd E:\\projects && " +
            "mvn archetype:generate -B -DarchetypeGroupId=io.vlingo " +
            "-DarchetypeArtifactId=vlingo-xoom-kubernetes-archetype " +
            "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
            "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
            "-DvlingoXoomServerVersion=1.2.9 -Dk8sPodName=starter-example-pod " +
            "-Dk8sImage=starter-example-image ";

    @Test
    public void testCommandPreparationWithBasicArchetype() {
        this.context.onProperties(loadBasicArchetypeProperties());
        final String[] commands = commandExecutorStep.prepareCommands(context);
        Assert.assertEquals(Terminal.active().initializationCommand(), commands[0]);
        Assert.assertEquals(Terminal.active().parameter(), commands[1]);
        Assert.assertEquals(EXPECTED_BASIC_ARCHETYPE_COMMAND, commands[2]);
    }

    private Properties loadBasicArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(ArchetypeProperties.VERSION.literal(), "1.0");
        properties.put(ArchetypeProperties.GROUP_ID.literal(), "io.vlingo");
        properties.put(ArchetypeProperties.ARTIFACT_ID.literal(), "starter-example");
        properties.put(ArchetypeProperties.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(ArchetypeProperties.XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(ArchetypeProperties.TARGET_FOLDER.literal(), "E:\\projects");
        return properties;
    }

    @Test
    public void testCommandPreparationWithKubernetesArchetype() {
        context.onProperties(loadKubernetesArchetypeProperties());
        final String[] commands = commandExecutorStep.prepareCommands(context);
        Assert.assertEquals(Terminal.active().initializationCommand(), commands[0]);
        Assert.assertEquals(Terminal.active().parameter(), commands[1]);
        Assert.assertEquals(EXPECTED_K8S_ARCHETYPE_COMMAND, commands[2]);
    }

    private Properties loadKubernetesArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(ArchetypeProperties.VERSION.literal(), "1.0");
        properties.put(ArchetypeProperties.GROUP_ID.literal(), "io.vlingo");
        properties.put(ArchetypeProperties.ARTIFACT_ID.literal(), "starter-example");
        properties.put(ArchetypeProperties.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(ArchetypeProperties.XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(ArchetypeProperties.TARGET_FOLDER.literal(), "E:\\projects");
        properties.put(ArchetypeProperties.DEPLOYMENT.literal(), "k8s");
        properties.put(ArchetypeProperties.KUBERNETES_IMAGE.literal(), "starter-example-image");
        properties.put(ArchetypeProperties.KUBERNETES_POD_NAME.literal(), "starter-example-pod");
        return properties;
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
        this.commandExecutorStep = new CommandExecutorStep();
    }

}
