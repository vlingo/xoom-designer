package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.DockerCommandManager;
import io.vlingo.xoom.starter.task.gui.UserInterfaceManager;
import io.vlingo.xoom.starter.task.projectgeneration.CommandLineGenerationManager;
import io.vlingo.xoom.starter.task.projectgeneration.DefaultGenerationManager;
import io.vlingo.xoom.starter.task.version.VersionDisplayManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.vlingo.xoom.starter.task.SubTask.*;
import static io.vlingo.xoom.starter.task.Task.*;

public class TaskTest {

    @Test
    public void testTaskRetrievalByCommand() {
        Assertions.assertEquals(CommandLineGenerationManager.class, Task.manage("gen", Arrays.asList("0")).getClass());
        Assertions.assertEquals(DefaultGenerationManager.class, Task.manage("gen", TaskExecutionContext.withoutOptions()).getClass());
        Assertions.assertEquals(UserInterfaceManager.class, Task.manage("gui", Arrays.asList("0")).getClass());
        Assertions.assertEquals(DockerCommandManager.class, Task.manage("dOckEr", Arrays.asList("0")).getClass());
        Assertions.assertEquals(VersionDisplayManager.class, Task.manage("-version", Arrays.asList("0")).getClass());
    }

    @Test
    public void testTaskRetrievalByUnknownCommand() {
        Assertions.assertThrows(UnknownCommandException.class, () -> {
            Task.manage("start", Arrays.asList("0"));
        });
    }

    @Test
    public void testDockerSubTaskRetrieval() {
        Assertions.assertEquals(DOCKER_PUSH, DOCKER.subTaskOf("push"));
        Assertions.assertEquals(DOCKER_PACKAGE, DOCKER.subTaskOf("package"));
        Assertions.assertEquals(DOCKER_STATUS, DOCKER.subTaskOf("status"));
    }

    @Test
    public void testUnknownSubTaskRetrieval() {
        Assertions.assertThrows(UnknownCommandException.class, () -> {
            DOCKER.subTaskOf("reset");
        });
    }
}
