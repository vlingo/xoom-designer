package io.vlingo.xoom.starter.task;

import org.junit.Test;

import java.util.Collections;

public class TaskExecutorTest {

    @Test(expected=CommandNotFoundException.class)
    public void testExecutionWithInvalidArgs() {
        TaskExecutor.execute(Collections.emptyList());
    }

}