package io.vlingo.xoom.starter.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.starter.task.SubTask.*;
import static io.vlingo.xoom.starter.task.Task.*;

public class TaskTest {

    @Test
    public void testTaskRetrievalByCommand() {
        Assertions.assertEquals(TEMPLATE_GENERATION, Task.trigger("gen"));
        Assertions.assertEquals(GRAPHICAL_USER_INTERFACE, Task.trigger("gui"));
        Assertions.assertEquals(DOCKER, Task.trigger("dOckEr"));
        Assertions.assertEquals(VERSION, Task.trigger("-version"));
    }

    @Test
    public void testTaskRetrievalByUnknownCommand() {
        Assertions.assertThrows(UnknownCommandException.class, () -> {
            Task.trigger("start");
        });
    }

    @Test
    public void testDockerSubTaskRetrieval() {
        Assertions.assertEquals(DOCKER_PUSH, Task.trigger("docker").subTaskOf("push"));
        Assertions.assertEquals(DOCKER_PACKAGE, Task.trigger("docker").subTaskOf("package"));
        Assertions.assertEquals(DOCKER_STATUS, Task.trigger("docker").subTaskOf("status"));
    }

    @Test
    public void testUnknownSubTaskRetrieval() {
        Assertions.assertThrows(UnknownCommandException.class, () -> {
            Task.trigger("docker").subTaskOf("reset");
        });
    }
}
