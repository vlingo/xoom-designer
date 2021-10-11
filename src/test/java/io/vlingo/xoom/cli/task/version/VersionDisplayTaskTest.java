package io.vlingo.xoom.cli.task.version;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class VersionDisplayTaskTest {

  @Test
  public void testThatTaskRuns() {
    new VersionDisplayTask().run(Arrays.asList("-version"));
  }

}
