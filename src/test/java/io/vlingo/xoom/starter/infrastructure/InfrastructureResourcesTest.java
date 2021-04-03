package io.vlingo.xoom.starter.infrastructure;

import io.vlingo.xoom.starter.Profile;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.StarterProperties;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.StarterServer;
import io.vlingo.xoom.starter.infrastructure.Infrastructure.UserInterface;
import io.vlingo.xoom.starter.task.projectgeneration.InvalidResourcesPathException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class InfrastructureResourcesTest {

    private static final String BASE_PATH = System.getProperty("user.dir");
    private static final String ROOT_FOLDER = Paths.get(BASE_PATH, "dist", "starter").toString();

    @Test
    public void testInfraResourcesAreResolved() {
        Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
        Infrastructure.resolveExternalResources(ExternalDirectory.from(BASE_PATH));
        Assertions.assertEquals(Paths.get(ROOT_FOLDER, "resources", "archetypes"), ArchetypesFolder.path());
        Assertions.assertEquals(19090, StarterProperties.retrieveServerPort(1));
        Assertions.assertEquals("http://localhost:19090", StarterServer.url().toString());
        // "xoom-designer": This will not work until a resource for it is created.
        Assertions.assertEquals("http://localhost:19090/context", UserInterface.rootContext());
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
        Profile.enableTestProfile();
        Infrastructure.clear();
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
        Infrastructure.clear();
    }
}
