package io.vlingo.xoom.starter.task.version;

import org.junit.jupiter.api.Test;

import java.util.Collections;

public class VersionDisplayManagerTest {

    @Test
    public void testVersionDisplay() {
        new VersionDisplayManager().run(Collections.emptyList());
    }

}
