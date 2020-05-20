package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.template.steps.ContentAvailability.DOCKER;
import static io.vlingo.xoom.starter.task.template.steps.ContentAvailability.KUBERNETES;
import static io.vlingo.xoom.starter.task.template.steps.DeploymentType.NONE;

public class ContentAvailabilityTest {

    private static final String ARTIFACT_ID = "xoom-app";
    private static final String TARGET_FOLDER = Paths.get("home", "projects").toString();
    private static final String EXPECTED_DOCKERFILE_PATH = Paths.get(TARGET_FOLDER, ARTIFACT_ID, "Dockerfile").toString();
    private static final String EXPECTED_KUBERNETES_PATH = Paths.get(TARGET_FOLDER, ARTIFACT_ID, "deployment").toString();

    @Test
    public void testContentAvailabilityVerification() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(loadProperties(NONE));

        Assertions.assertFalse(DOCKER.shouldBeAvailable(context));
        Assertions.assertFalse(KUBERNETES.shouldBeAvailable(context));

        context.onProperties(loadProperties(DeploymentType.DOCKER));

        Assertions.assertTrue(DOCKER.shouldBeAvailable(context));
        Assertions.assertFalse(KUBERNETES.shouldBeAvailable(context));

        context.onProperties(loadProperties(DeploymentType.KUBERNETES));

        Assertions.assertTrue(DOCKER.shouldBeAvailable(context));
        Assertions.assertTrue(KUBERNETES.shouldBeAvailable(context));
    }

    @Test
    public void testContentAvailabilityPathResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(loadProperties());

        Assertions.assertEquals(EXPECTED_DOCKERFILE_PATH, DOCKER.resolvePath(context));
        Assertions.assertEquals(EXPECTED_KUBERNETES_PATH, KUBERNETES.resolvePath(context));
    }

    @Test
    public void testContentAvailabilityRetrieval() {
        final List<ContentAvailability> availabilities = ContentAvailability.availabilities();
        Assertions.assertEquals(2, availabilities.size());
        Assertions.assertTrue(availabilities.contains(DOCKER));
        Assertions.assertTrue(availabilities.contains(KUBERNETES));
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        properties.put(Property.ARTIFACT_ID.literal(), ARTIFACT_ID);
        properties.put(Property.TARGET_FOLDER.literal(), TARGET_FOLDER);
        return properties;
    }

    private Properties loadProperties(final DeploymentType deploymentType) {
        final Properties properties = new Properties();
        properties.put(Property.DEPLOYMENT.literal(), deploymentType.name());
        return properties;
    }

}
