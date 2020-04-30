package io.vlingo.xoom.starter.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class TaskExecutorTest {

    @Test
    public void testExecutionWithInvalidArgs() {
        Assertions.assertThrows(CommandNotFoundException.class,() -> {
            TaskExecutor.execute(Collections.emptyList());
        });
    }

}