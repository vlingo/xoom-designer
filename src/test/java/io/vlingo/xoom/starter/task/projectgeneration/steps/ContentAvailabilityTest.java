package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.codegen.parameter.Label.DEPLOYMENT;
import static io.vlingo.xoom.starter.task.Agent.TERMINAL;
import static io.vlingo.xoom.starter.task.Agent.WEB;
import static io.vlingo.xoom.starter.task.projectgeneration.steps.ContentAvailability.DOCKER;
import static io.vlingo.xoom.starter.task.projectgeneration.steps.ContentAvailability.KUBERNETES;

public class ContentAvailabilityTest {

    private static final String ARTIFACT_ID = "xoom-app";
    private static final String TARGET_FOLDER = Paths.get("home", "projects", "xoom-app").toString();
    private static final String EXPECTED_DOCKERFILE_PATH = Paths.get(TARGET_FOLDER, "Dockerfile").toString();
    private static final String EXPECTED_KUBERNETES_PATH = Paths.get(TARGET_FOLDER, "deployment").toString();

    @Test
    public void testContentAvailabilityVerification() {
        final TaskExecutionContext noneDeployment =
                TaskExecutionContext.executedFrom(TERMINAL)
                        .with(CodeGenerationParameters.from(DEPLOYMENT, DeploymentType.NONE));

        Assertions.assertFalse(DOCKER.shouldBeAvailable(noneDeployment));
        Assertions.assertFalse(KUBERNETES.shouldBeAvailable(noneDeployment));

        final TaskExecutionContext dockerDeploymentContext =
                TaskExecutionContext.executedFrom(WEB)
                        .with(CodeGenerationParameters.from(DEPLOYMENT, DeploymentType.DOCKER));

        Assertions.assertTrue(DOCKER.shouldBeAvailable(dockerDeploymentContext));
        Assertions.assertFalse(KUBERNETES.shouldBeAvailable(dockerDeploymentContext));

        final TaskExecutionContext k8sDeploymentContext =
                TaskExecutionContext.executedFrom(WEB)
                        .with(CodeGenerationParameters.from(DEPLOYMENT, DeploymentType.KUBERNETES));

        Assertions.assertTrue(DOCKER.shouldBeAvailable(k8sDeploymentContext));
        Assertions.assertTrue(KUBERNETES.shouldBeAvailable(k8sDeploymentContext));
    }

    @Test
    public void testContentAvailabilityPathResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.executedFrom(TERMINAL)
                .with(CodeGenerationParameters.from(Label.ARTIFACT_ID, ARTIFACT_ID)
                        .add(Label.TARGET_FOLDER, TARGET_FOLDER));

        Assertions.assertEquals(EXPECTED_DOCKERFILE_PATH, DOCKER.resolvePath(context));
        Assertions.assertEquals(EXPECTED_KUBERNETES_PATH, KUBERNETES.resolvePath(context));
    }

    @Test
    public void testContentAvailabilityRetrieval() {
        final List<ContentAvailability> availabilities = ContentAvailability.availabilities();
        Assertions.assertEquals(3, availabilities.size());
        Assertions.assertTrue(availabilities.contains(DOCKER));
        Assertions.assertTrue(availabilities.contains(KUBERNETES));
    }

}
