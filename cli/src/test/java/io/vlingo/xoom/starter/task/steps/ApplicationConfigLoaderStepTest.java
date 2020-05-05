package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.ApplicationConfiguration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ApplicationConfigLoaderStepTest {

    private static final String EXPECTED_URL = "http://localhost:8080/xoom-starter";

    @Test
    public void testConfigurationLoad() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        new ApplicationConfigLoaderStep().process(context);

        Assertions.assertEquals(EXPECTED_URL, context.configurationOf(ApplicationConfiguration.USER_INTERFACE_CONFIG_KEY));
    }

}
