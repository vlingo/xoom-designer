package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.io.File.separator;

public class ResourceLocationStepTest {

    private static final String ROOT_FOLDER = separator + "starter" + separator;

    private TaskExecutionContext context;
    private ResourcesLocationStep resourcesLocationStep;

    @Test
    public void testResourceLocationStepWithAlreadyExistingPaths() {
        Resource.rootIn(ROOT_FOLDER);
        resourcesLocationStep.process(context);
        Assertions.assertEquals(ROOT_FOLDER + "vlingo-xoom-starter.properties", Resource.STARTER_PROPERTIES_FILE.path());
        Assertions.assertEquals(ROOT_FOLDER + "resources" + separator + "archetypes", Resource.ARCHETYPES_FOLDER.path());
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        this.context = TaskExecutionContext.withoutOptions();
        this.resourcesLocationStep = new ResourcesLocationStep();
    }

}
