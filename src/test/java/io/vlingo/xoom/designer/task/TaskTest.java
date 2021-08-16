package io.vlingo.xoom.designer.task;

import io.vlingo.xoom.designer.infrastructure.userinterface.UserInterfaceManager;
import io.vlingo.xoom.designer.task.version.VersionDisplayManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TaskTest {

  @Test
  public void testTaskRetrievalByCommand() {
    Assertions.assertEquals(UserInterfaceManager.class, Task.of("gui", Arrays.asList("0")).getClass());
    Assertions.assertEquals(VersionDisplayManager.class, Task.of("-version", Arrays.asList("0")).getClass());
  }

  @Test
  public void testTaskRetrievalByUnknownCommand() {
    Assertions.assertThrows(UnknownCommandException.class, () -> {
      Task.of("start", Arrays.asList("0"));
    });
  }

}
