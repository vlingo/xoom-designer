package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.template.Terminal.*;

public class ArchetypeCommandResolverStepTest {


    private TaskExecutionContext context;
    private ArchetypeCommandResolverStep archetypeCommandResolverStep;

    private static final String WINDOWS_ROOT_FOLDER = Paths.get("D:", "tools", "starter").toString();

    private static final String DEFAULT_ROOT_FOLDER = Paths.get("home", "tools", "starter").toString();

    private static final String WINDOWS_ARCHETYPE_PATH =
            Paths.get(WINDOWS_ROOT_FOLDER, "resources", "archetypes", "kubernetes-archetype").toString();

    private static final String DEFAULT_ARCHETYPE_PATH =
            Paths.get(DEFAULT_ROOT_FOLDER, "resources", "archetypes", "kubernetes-archetype").toString();

    private static final String EXPECTED_ARCHETYPE_COMMAND_ON_WINDOWS =
                    "D: && cd " + WINDOWS_ARCHETYPE_PATH + " && mvn clean install" +
                    " && E: && cd E:\\projects && mvn archetype:generate -B -DarchetypeCatalog=internal -DarchetypeGroupId=io.vlingo " +
                    "-DarchetypeArtifactId=vlingo-xoom-kubernetes-archetype " +
                    "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
                    "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
                    "-DvlingoXoomServerVersion=1.2.9 -DdockerImage=starter-example-image " +
                    "-Dk8sPodName=starter-example-pod -Dk8sImage=starter-example-image ";

    private static final String EXPECTED_ARCHETYPE_COMMAND =
                    "cd " + DEFAULT_ARCHETYPE_PATH +  " && mvn clean install && cd /home/projects && " +
                    "mvn archetype:generate -B -DarchetypeCatalog=internal -DarchetypeGroupId=io.vlingo " +
                    "-DarchetypeArtifactId=vlingo-xoom-kubernetes-archetype " +
                    "-DarchetypeVersion=1.0 -Dversion=1.0 -DgroupId=io.vlingo " +
                    "-DartifactId=starter-example -Dpackage=io.vlingo.starterexample " +
                    "-DvlingoXoomServerVersion=1.2.9 -DdockerImage=starter-example-image " +
                    "-Dk8sPodName=starter-example-pod -Dk8sImage=starter-example-image ";

    @Test
    public void testCommandPreparationWithKubernetesArchetypeOnWindows() {
        Terminal.enable(WINDOWS);
        Resource.rootIn(WINDOWS_ROOT_FOLDER);
        context.onProperties(loadKubernetesArchetypeProperties("E:\\projects"));
        archetypeCommandResolverStep.process(context);
        final String[] commands = context.commands();
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commands[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commands[1]);
        Assertions.assertEquals(EXPECTED_ARCHETYPE_COMMAND_ON_WINDOWS, commands[2]);
    }

    @Test
    public void testCommandPreparationWithKubernetesArchetypeOnLinux() {
        Terminal.enable(LINUX);
        Resource.rootIn(DEFAULT_ROOT_FOLDER);
        context.onProperties(loadKubernetesArchetypeProperties("/home/projects"));
        archetypeCommandResolverStep.process(context);
        final String[] commands = context.commands();
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commands[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commands[1]);
        Assertions.assertEquals(EXPECTED_ARCHETYPE_COMMAND, commands[2]);
    }

    @Test
    public void testCommandPreparationWithKubernetesArchetypeOnMac() {
        Terminal.enable(MAC_OS);
        Resource.rootIn(DEFAULT_ROOT_FOLDER);
        context.onProperties(loadKubernetesArchetypeProperties("/home/projects"));
        archetypeCommandResolverStep.process(context);
        final String[] commands = context.commands();
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commands[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commands[1]);
        Assertions.assertEquals(EXPECTED_ARCHETYPE_COMMAND, commands[2]);
    }

    private Properties loadKubernetesArchetypeProperties(final String targetFolder) {
        final Properties properties = new Properties();
        properties.put(Property.VERSION.literal(), "1.0");
        properties.put(Property.GROUP_ID.literal(), "io.vlingo");
        properties.put(Property.ARTIFACT_ID.literal(), "starter-example");
        properties.put(Property.PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(Property.XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(Property.TARGET_FOLDER.literal(), targetFolder);
        properties.put(Property.DEPLOYMENT.literal(), "k8s");
        properties.put(Property.DOCKER_IMAGE.literal(), "starter-example-image");
        properties.put(Property.KUBERNETES_IMAGE.literal(), "starter-example-image");
        properties.put(Property.KUBERNETES_POD_NAME.literal(), "starter-example-pod");
        return properties;
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        this.archetypeCommandResolverStep = new ArchetypeCommandResolverStep();
        this.context = TaskExecutionContext.withoutOptions();
    }

    @AfterEach
    public void clear() {
        Terminal.disable();
    }

}
