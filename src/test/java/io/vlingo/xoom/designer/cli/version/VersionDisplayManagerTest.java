package io.vlingo.xoom.designer.cli.version;

import io.vlingo.xoom.cli.task.version.VersionDisplayManager;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class VersionDisplayManagerTest {

    @Test
    public void testVersionDisplay() {
        new VersionDisplayManager().run(Collections.emptyList());
    }

}
