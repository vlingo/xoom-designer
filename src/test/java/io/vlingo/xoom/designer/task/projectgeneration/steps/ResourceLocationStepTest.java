package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.DesignerProperties;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ResourceLocationStepTest {

    private TaskExecutionContext context;
    private ResourcesLocationStep resourcesLocationStep;

    @Test
    public void testResourceLocationStepWithAlreadyExistingPaths() {
        resourcesLocationStep.process(context);
        Assertions.assertEquals(1, DesignerProperties.retrieveServerPort(1));
        Assertions.assertEquals(Paths.get(System.getProperty("user.dir"), "dist", "designer", "resources", "archetypes"), ArchetypesFolder.path());
    }

    @BeforeEach
    public void setUp() {
        Infrastructure.clear();
        Profile.enableTestProfile();
        this.context = TaskExecutionContext.withoutOptions();
        this.resourcesLocationStep = new ResourcesLocationStep();
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
    }

}
