package io.vlingo.xoom.starter.infrastructure;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.StarterProperties;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.StarterServer;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.UserInterface;
import io.vlingo.xoom.starter.task.projectgeneration.InvalidResourcesPathException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class InfrastructureTest {

    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String ROOT_FOLDER = Paths.get(BASE_PATH, "dist", "starter").toString();

    @Test
    public void testInfraResourcesAreResolved() {
        Infrastructure.resolveInternalResources(HomeDirectory.from(ROOT_FOLDER));
        Infrastructure.resolveExternalResources(ExternalDirectory.from(BASE_PATH));
        Assertions.assertEquals(Paths.get(ROOT_FOLDER, "resources", "archetypes"), ArchetypesFolder.path());
        Assertions.assertEquals(19090, StarterProperties.retrieveServerPort(1));
        Assertions.assertEquals("http://127.0.0.1:19090", StarterServer.url().toString());
        Assertions.assertEquals("http://127.0.0.1:19090/xoom-starter", UserInterface.rootContext());
        Assertions.assertFalse(Infrastructure.XoomProperties.properties().isEmpty());
    }

    @Test
    public void testNullPathUpdate() {
        Assertions.assertThrows(InvalidResourcesPathException.class, () -> {
            Infrastructure.resolveInternalResources(HomeDirectory.from(null));
        });
    }

    @BeforeEach
    public void setUp() {
        Configuration.enableTestProfile();
        Infrastructure.clear();
    }

    @BeforeEach
    public void clear() {
        Configuration.disableTestProfile();
        Infrastructure.clear();
    }
}
