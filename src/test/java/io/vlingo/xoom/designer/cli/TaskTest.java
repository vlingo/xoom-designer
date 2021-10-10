package io.vlingo.xoom.designer.cli;

import io.vlingo.xoom.cli.UnknownCommandException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TaskTest {

  @Test
  public void testTaskRetrievalByCommand() {
    //Assertions.assertEquals(UserInterfaceManager.class, Task.managerOf("gui", Arrays.asList("0")).getClass());
    //Assertions.assertEquals(DockerCommandManager.class, Task.managerOf("dOckEr", Arrays.asList("0")).getClass());
    //Assertions.assertEquals(VersionDisplayManager.class, Task.managerOf("-version", Arrays.asList("0")).getClass());
  }

  @Test
  public void testTaskRetrievalByUnknownCommand() {
    Assertions.assertThrows(UnknownCommandException.class, () -> {
      //Task.managerOf("start", Arrays.asList("0"));
    });
  }

  @Test
  public void testDockerSubTaskRetrieval() {
    //Assertions.assertEquals(DOCKER_PUSH, DOCKER.subTaskOf("push"));
//    Assertions.assertEquals(DOCKER_PACKAGE, DOCKER.subTaskOf("package"));
//    Assertions.assertEquals(DOCKER_STATUS, DOCKER.subTaskOf("status"));
  }

  @Test
  public void testUnknownSubTaskRetrieval() {
    Assertions.assertThrows(UnknownCommandException.class, () -> {
//      DOCKER.subTaskOf("reset");
    });
  }
}
