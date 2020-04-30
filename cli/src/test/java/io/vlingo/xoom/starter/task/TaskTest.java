package io.vlingo.xoom.starter.task;

import org.junit.Assert;
import org.junit.Test;

import static io.vlingo.xoom.starter.task.SubTask.*;
import static io.vlingo.xoom.starter.task.Task.DOCKER;
import static io.vlingo.xoom.starter.task.Task.TEMPLATE_GENERATION;

public class TaskTest {

    @Test
    public void testDockerTaskRetrievalByCommand() {
        Assert.assertEquals(DOCKER, Task.trigger("dOckEr"));
    }

    @Test
    public void testTemplateGenerationTaskRetrievalByCommand() {
        Assert.assertEquals(TEMPLATE_GENERATION, Task.trigger("gen"));
    }

    @Test(expected = UnknownCommandException.class)
    public void testTaskRetrievalByUnknownCommand() {
        Task.trigger("start");
    }

    @Test
    public void testDockerSubTaskRetrieval() {
        Assert.assertEquals(DOCKER_PUSH, Task.trigger("docker").subTaskOf("push"));
        Assert.assertEquals(DOCKER_PACKAGE, Task.trigger("docker").subTaskOf("package"));
        Assert.assertEquals(DOCKER_STATUS, Task.trigger("docker").subTaskOf("status"));
    }

    @Test(expected = UnknownCommandException.class)
    public void testUnknownSubTaskRetrieval() {
        Task.trigger("docker").subTaskOf("reset");
    }
}
