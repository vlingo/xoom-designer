package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;

public class ModelGenerationStepTest {

    @Test
    public void testModelGeneration() {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        context.onProperties(loadProperties());
        new ModelGenerationStep().process(context);

        Assertions.assertEquals(11, context.contents().size());
        Assertions.assertEquals("Order.java", context.contents().get(0).file.getName());
        Assertions.assertEquals("OrderEntity.java", context.contents().get(1).file.getName());
        Assertions.assertEquals("OrderState.java", context.contents().get(2).file.getName());
        Assertions.assertEquals("OrderPlaceholderDefined.java", context.contents().get(3).file.getName());
        Assertions.assertEquals("OrderCreated.java", context.contents().get(4).file.getName());
        Assertions.assertEquals("OrderCancelled.java", context.contents().get(5).file.getName());
        Assertions.assertEquals("Product.java", context.contents().get(6).file.getName());
        Assertions.assertEquals("ProductEntity.java", context.contents().get(7).file.getName());
        Assertions.assertEquals("ProductState.java", context.contents().get(8).file.getName());
        Assertions.assertEquals("ProductPlaceholderDefined.java", context.contents().get(9).file.getName());
        Assertions.assertEquals("ProductSoldOut.java", context.contents().get(10).file.getName());

        Assertions.assertTrue(context.contents().get(0).text.contains("interface Order "));
        Assertions.assertTrue(context.contents().get(1).text.contains("class OrderEntity extends StatefulEntity"));
        Assertions.assertTrue(context.contents().get(2).text.contains("class OrderState extends StateObject"));
        Assertions.assertTrue(context.contents().get(3).text.contains("class OrderPlaceholderDefined extends DomainEvent"));
        Assertions.assertTrue(context.contents().get(4).text.contains("class OrderCreated extends DomainEvent"));
        Assertions.assertTrue(context.contents().get(5).text.contains("class OrderCancelled extends DomainEvent"));
        Assertions.assertTrue(context.contents().get(6).text.contains("interface Product "));
        Assertions.assertTrue(context.contents().get(7).text.contains("class ProductEntity extends StatefulEntity"));
        Assertions.assertTrue(context.contents().get(8).text.contains("class ProductState extends StateObject"));
        Assertions.assertTrue(context.contents().get(9).text.contains("class ProductPlaceholderDefined extends DomainEvent"));
        Assertions.assertTrue(context.contents().get(10).text.contains("class ProductSoldOut extends DomainEvent"));
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
