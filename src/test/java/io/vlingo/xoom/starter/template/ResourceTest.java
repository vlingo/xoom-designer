package io.vlingo.xoom.starter.template;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.io.File.separator;

public class ResourceTest {

    @Test
    public void testPathUpdate() {
        final String root = separator + "starter" + separator;
        Resource.rootIn(root);
        Assert.assertEquals(root + "vlingo-xoom-starter.properties", Resource.PROPERTIES_FILE.path());
        Assert.assertEquals(root + "resources" + separator + "archetypes", Resource.ARCHETYPES_FOLDER.path());
    }

    @Test(expected = InvalidResourcesPathException.class)
    public void testNullPathUpdate() {
        Resource.rootIn(null);
    }

}
