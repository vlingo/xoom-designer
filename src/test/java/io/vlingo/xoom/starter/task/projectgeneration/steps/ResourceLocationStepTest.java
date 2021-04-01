package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.infrastructure.HomeDirectory;
import io.vlingo.xoom.starter.infrastructure.Infrastructure;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.StarterProperties;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ResourceLocationStepTest {

    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    private TaskExecutionContext context;
    private ResourcesLocationStep resourcesLocationStep;

    @Test
    public void testResourceLocationStepWithAlreadyExistingPaths() {
        Infrastructure.resolveInternalResources(HomeDirectory.from(ROOT_FOLDER));
        resourcesLocationStep.process(context);
        Assertions.assertEquals(19090, StarterProperties.retrieveServerPort(1));
        Assertions.assertEquals(Paths.get(ROOT_FOLDER, "resources", "archetypes"), ArchetypesFolder.path());
    }

    @BeforeEach
    public void setUp() {
        Infrastructure.clear();
        this.context = TaskExecutionContext.withoutOptions();
        this.resourcesLocationStep = new ResourcesLocationStep();
    }

}
