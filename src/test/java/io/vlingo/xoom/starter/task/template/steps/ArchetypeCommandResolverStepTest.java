package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.steps.ArchetypeCommandResolverStep;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Properties;

public class ArchetypeCommandResolverStepTest {

    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    private TaskExecutionContext context;
    private ArchetypeCommandResolverStep archetypeCommandResolverStep;

    private static final String EXPECTED_BASIC_ARCHETYPE_COMMAND =
            "cd " + Paths.get(ROOT_FOLDER, "resources", "archetypes", "basic-archetype").toString() + " && " +
                    "mvn clean install && cd E:\\projects && " +
                    "mvn archetype:generate -B -DarchetypeCatalog=internal -DarchetypeGroupId=io.vlingo " +
                    "-DarchetypeArtifactId=vlingo-xoom-basic-archetype " +
                    "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
                    "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
                    "-DvlingoXoomServerVersion=1.2.9 ";

    private static final String EXPECTED_K8S_ARCHETYPE_COMMAND =
            "cd " + Paths.get(ROOT_FOLDER, "resources", "archetypes", "kubernetes-archetype").toString() + " && " +
                    "mvn clean install && cd E:\\projects && " +
                    "mvn archetype:generate -B -DarchetypeCatalog=internal -DarchetypeGroupId=io.vlingo " +
                    "-DarchetypeArtifactId=vlingo-xoom-kubernetes-archetype " +
                    "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
                    "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
                    "-DvlingoXoomServerVersion=1.2.9 -DdockerImage=starter-example-image " +
                    "-Dk8sPodName=starter-example-pod -Dk8sImage=starter-example-image ";

    @Test
    public void testCommandPreparationWithBasicArchetype() {
        this.context.onProperties(loadBasicArchetypeProperties());
        archetypeCommandResolverStep.process(context);
        final String[] commands = context.commands();
        Assert.assertEquals(Terminal.active().initializationCommand(), commands[0]);
        Assert.assertEquals(Terminal.active().parameter(), commands[1]);
        Assert.assertEquals(EXPECTED_BASIC_ARCHETYPE_COMMAND, commands[2]);
    }

    private Properties loadBasicArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(Property.VERSION.literal(), "1.0");
        properties.put(Property.GROUP_ID.literal(), "io.vlingo");
        properties.put(Property.ARTIFACT_ID.literal(), "starter-example");
        properties.put(Property.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(Property.XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(Property.TARGET_FOLDER.literal(), "E:\\projects");
        return properties;
    }

    @Test
    public void testCommandPreparationWithKubernetesArchetype() {
        context.onProperties(loadKubernetesArchetypeProperties());
        archetypeCommandResolverStep.process(context);
        final String[] commands = context.commands();
        Assert.assertEquals(Terminal.active().initializationCommand(), commands[0]);
        Assert.assertEquals(Terminal.active().parameter(), commands[1]);
        Assert.assertEquals(EXPECTED_K8S_ARCHETYPE_COMMAND, commands[2]);
    }

    private Properties loadKubernetesArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(Property.VERSION.literal(), "1.0");
        properties.put(Property.GROUP_ID.literal(), "io.vlingo");
        properties.put(Property.ARTIFACT_ID.literal(), "starter-example");
        properties.put(Property.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(Property.XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(Property.TARGET_FOLDER.literal(), "E:\\projects");
        properties.put(Property.DEPLOYMENT.literal(), "k8s");
        properties.put(Property.DOCKER_IMAGE.literal(), "starter-example-image");
        properties.put(Property.KUBERNETES_IMAGE.literal(), "starter-example-image");
        properties.put(Property.KUBERNETES_POD_NAME.literal(), "starter-example-pod");
        return properties;
    }

    @Before
    public void setUp() {
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
        this.archetypeCommandResolverStep = new ArchetypeCommandResolverStep();
        this.context = TaskExecutionContext.withoutOptions();
    }

}
