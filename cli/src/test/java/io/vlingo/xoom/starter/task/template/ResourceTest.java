package io.vlingo.xoom.starter.task.template;

import io.vlingo.xoom.starter.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.io.File.separator;

public class ResourceTest {

    private final static String ROOT_FOLDER = separator + "starter" + separator;

    @Test
    public void testPathUpdate() {
        Resource.rootIn(ROOT_FOLDER);
        Assertions.assertTrue(Resource.hasAllPaths());
        Assertions.assertEquals(ROOT_FOLDER + "vlingo-xoom-starter.properties", Resource.STARTER_PROPERTIES_FILE.path());
        Assertions.assertEquals(ROOT_FOLDER + "resources" + separator + "archetypes", Resource.ARCHETYPES_FOLDER.path());
    }

    @Test
    public void testNullPathUpdate() {
        Assertions.assertThrows(InvalidResourcesPathException.class, () -> {
            Resource.rootIn(null);
        });
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
    }

}
