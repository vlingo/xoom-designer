package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.codegen.InvalidResourcesPathException;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class InfrastructureResourcesTest {

  private static final String BASE_PATH = System.getProperty("user.dir");
  private static final String ROOT_FOLDER = Paths.get(BASE_PATH, "dist", "designer").toString();

  @BeforeEach
  public void setUp() {
    Profile.enableTestProfile();
    Infrastructure.clear();
  }

  @Test
  public void testInfraResourcesAreResolved() {
    Infrastructure.setupResources(HomeDirectory.fromEnvironment(), 19090);
    final DesignerServerConfiguration designerServerConfiguration = ComponentRegistry.withType(DesignerServerConfiguration.class);
    Assertions.assertEquals(19090, designerServerConfiguration.port());
    Assertions.assertEquals("http://localhost:19090/context", designerServerConfiguration.resolveUserInterfaceURL().toString());
    Assertions.assertEquals(Paths.get(ROOT_FOLDER, "staging"), StagingFolder.path());
  }

  @Test
  public void testNullPathUpdate() {
    Assertions.assertThrows(InvalidResourcesPathException.class, () -> {
      Infrastructure.setupResources(HomeDirectory.from(null), 9019);
    });
  }

  @AfterAll
  public static void clear() {
    Profile.disableTestProfile();
    Infrastructure.clear();
  }
}
