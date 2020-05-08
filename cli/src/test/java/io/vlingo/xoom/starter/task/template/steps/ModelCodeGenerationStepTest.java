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
        Assertions.assertEquals("OrderEntity.java", context.outputResources().get(0).file.getName());
        Assertions.assertEquals("Order.java", context.outputResources().get(1).file.getName());
        Assertions.assertEquals("OrderState.java", context.outputResources().get(2).file.getName());
        Assertions.assertEquals("OrderCreated.java", context.outputResources().get(3).file.getName());
        Assertions.assertEquals("OrderCancelled.java", context.outputResources().get(4).file.getName());
        Assertions.assertEquals("OrderPlaceholderDefined.java", context.outputResources().get(5).file.getName());
        Assertions.assertEquals("ProductEntity.java", context.outputResources().get(6).file.getName());
        Assertions.assertEquals("Product.java", context.outputResources().get(7).file.getName());
        Assertions.assertEquals("ProductState.java", context.outputResources().get(8).file.getName());
        Assertions.assertEquals("ProductSoldOut.java", context.outputResources().get(9).file.getName());
        Assertions.assertEquals("ProductPlaceholderDefined.java", context.outputResources().get(10).file.getName());

        Assertions.assertTrue(context.outputResources().get(0).content.contains("class OrderEntity extends StatefulEntity"));
        Assertions.assertTrue(context.outputResources().get(1).content.contains("interface Order "));
        Assertions.assertTrue(context.outputResources().get(2).content.contains("class OrderState extends StateObject"));
        Assertions.assertTrue(context.outputResources().get(3).content.contains("class OrderCreated extends DomainEvent"));
        Assertions.assertTrue(context.outputResources().get(4).content.contains("class OrderCancelled extends DomainEvent"));
        Assertions.assertTrue(context.outputResources().get(5).content.contains("class OrderPlaceholderDefined extends DomainEvent"));
        Assertions.assertTrue(context.outputResources().get(6).content.contains("class ProductEntity extends StatefulEntity"));
        Assertions.assertTrue(context.outputResources().get(7).content.contains("interface Product "));
        Assertions.assertTrue(context.outputResources().get(8).content.contains("class ProductState extends StateObject"));
        Assertions.assertTrue(context.outputResources().get(9).content.contains("class ProductSoldOut extends DomainEvent"));
        Assertions.assertTrue(context.outputResources().get(10).content.contains("class ProductPlaceholderDefined extends DomainEvent"));
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
