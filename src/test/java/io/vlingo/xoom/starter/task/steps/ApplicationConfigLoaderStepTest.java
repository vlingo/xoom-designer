package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ApplicationConfigLoaderStepTest {

    private static final String EXPECTED_URL = "http://localhost:19090/xoom-starter";

    @Test
    public void testConfigurationLoad() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        new ApplicationConfigLoaderStep().process(context);

        Assertions.assertEquals(EXPECTED_URL, context.configurationOf(Configuration.USER_INTERFACE_CONFIG_KEY));
    }

}
