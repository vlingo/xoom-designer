package io.vlingo.xoom.designer.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class TaskExecutorTest {

    @Test
    public void testExecutionWithInvalidArgs() {
        System.out.println("===========================");
        System.out.println("Expected exception follows:");
        System.out.println("===========================");

        Assertions.assertThrows(CommandNotFoundException.class,() -> {
            TaskExecutor.execute(Collections.emptyList());
        });
    }

}