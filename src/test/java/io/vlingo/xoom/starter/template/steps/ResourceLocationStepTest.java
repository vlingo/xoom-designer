package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.template.InvalidResourcesPathException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.io.File.separator;

public class ResourceLocationStepTest {

    private static final String ROOT_FOLDER = separator + "starter" + separator;

    private TaskExecutionContext context;
    private ResourcesLocationStep resourcesLocationStep;

    @Test
    public void testResourceLocationStepWithAlreadyExistingPaths() {
        Resource.rootIn(ROOT_FOLDER);
        resourcesLocationStep.process(context);
        Assert.assertEquals(ROOT_FOLDER + "vlingo-xoom-starter.properties", Resource.STARTER_PROPERTIES_FILE.path());
        Assert.assertEquals(ROOT_FOLDER + "resources" + separator + "archetypes", Resource.ARCHETYPES_FOLDER.path());
    }

    @Test(expected = InvalidResourcesPathException.class)
    public void testResourceLocationStepWithNullRootPath() {
        resourcesLocationStep.process(context);
    }

    @Before
    public void setUp() {
        Resource.clear();
        this.context = new TaskExecutionContext();
        this.resourcesLocationStep = new ResourcesLocationStep();
    }

}
