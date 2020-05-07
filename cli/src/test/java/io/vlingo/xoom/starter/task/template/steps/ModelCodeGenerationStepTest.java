package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;

public class ModelCodeGenerationStepTest {

    @Test
    public void testModelClassesGeneration() {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        context.onProperties(loadProperties());
        new ModelCodeGenerationStep().process(context);
        Assertions.assertEquals(11, context.outputResources().size());
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "com.company.context");
        properties.put(ARTIFACT_ID.literal(), "logistics");
        properties.put(STORAGE_TYPE.literal(), "STATE_STORE");
        properties.put(AGGREGATES.literal(), "Order;OrderCreated;OrderCancelled|Product;ProductSoldOut");
        properties.put(TARGET_FOLDER.literal(), "/Projects/");
        return properties;
    }

}
