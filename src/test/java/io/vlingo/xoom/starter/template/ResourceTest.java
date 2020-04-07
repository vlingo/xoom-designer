package io.vlingo.xoom.starter.template;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.io.File.separator;

public class ResourceTest {

    private final static String ROOT_FOLDER = separator + "starter" + separator;

    @Test
    public void testPathUpdate() {
        Resource.rootIn(ROOT_FOLDER);
        Assert.assertEquals(ROOT_FOLDER + "vlingo-xoom-starter.properties", Resource.PROPERTIES_FILE.path());
        Assert.assertEquals(ROOT_FOLDER + "resources" + separator + "archetypes", Resource.ARCHETYPES_FOLDER.path());
    }

    @Test(expected = InvalidResourcesPathException.class)
    public void testNullPathUpdate() {
        Resource.rootIn(null);
    }

    @Before
    public void setUp() {
        Resource.clear();
    }

}
