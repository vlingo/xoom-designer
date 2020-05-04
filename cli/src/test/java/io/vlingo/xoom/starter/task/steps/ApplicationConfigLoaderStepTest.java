package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.ApplicationConfigLoaderStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.starter.ApplicationConfiguration.USER_INTERFACE;

public class ApplicationConfigLoaderStepTest {

    private static final String EXPECTED_URL = "http://localhost:8080/xoom-starter";

    @Test
    public void testConfigurationLoad() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        new ApplicationConfigLoaderStep().process(context);

        Assertions.assertEquals(EXPECTED_URL, context.configurationOf(USER_INTERFACE));
    }

}
