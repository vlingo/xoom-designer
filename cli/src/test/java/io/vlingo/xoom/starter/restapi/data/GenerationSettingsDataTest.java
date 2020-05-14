package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Task;
import io.vlingo.xoom.starter.task.template.Agent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.vlingo.xoom.starter.task.Property.*;

public class GenerationSettingsDataTest {

    @Test
    public void testArgumentsConversion() {
        final List<AggregateData> aggregates = Arrays.asList(
                new AggregateData("Order",
                        Arrays.asList(new DomainEventData("OrderCreated"),
                                new DomainEventData("OrderCancelled"))),

                new AggregateData("Product",
                        Arrays.asList(new DomainEventData("ProductSoldOut")))
        );

        final List<AggregateData> restResources = Arrays.asList(
                new AggregateData("Order", Collections.emptyList()),
                new AggregateData("Product", Collections.emptyList())
        );

        final ModelSettingsData model =
                new ModelSettingsData("STATE_STORE", true, true, "None",
                        "MariaDB", "Postgres", aggregates, restResources);

        final DeploymentSettingsData deployment =
                new DeploymentSettingsData(3, "k8s", "xoom-app",
                        "dambrosio/xoom-app", "xoom-app-pod");

        final ContextSettingsData context =
                new ContextSettingsData("com.company", "xoom-application", "1.0",
                        "com.company.business", "1.3.0");

        final GenerationSettingsData settings =
                new GenerationSettingsData(context, model, deployment,
                        "/home/projects/xoom-app");

        final List<String> args = settings.toArguments();

        Assertions.assertEquals(Task.TEMPLATE_GENERATION.command(), args.get(0));
        Assertions.assertEquals(GROUP_ID.literal(), args.get(1));
        Assertions.assertEquals("com.company", args.get(2));
        Assertions.assertEquals(ARTIFACT_ID.literal(), args.get(3));
        Assertions.assertEquals("xoom-application", args.get(4));
        Assertions.assertEquals(VERSION.literal(), args.get(5));
        Assertions.assertEquals("1.0", args.get(6));
        Assertions.assertEquals(PACKAGE.literal(), args.get(7));
        Assertions.assertEquals("com.company.business", args.get(8));
        Assertions.assertEquals(XOOM_SERVER_VERSION.literal(), args.get(9));
        Assertions.assertEquals("1.3.0", args.get(10));
        Assertions.assertEquals(STORAGE_TYPE.literal(), args.get(11));
        Assertions.assertEquals("STATE_STORE", args.get(12));
        Assertions.assertEquals(AGGREGATES.literal(), args.get(13));
        Assertions.assertEquals("Order;OrderCreated;OrderCancelled|Product;ProductSoldOut", args.get(14));
        Assertions.assertEquals(REST_RESOURCES.literal(), args.get(15));
        Assertions.assertEquals("Order;Product", args.get(16));
        Assertions.assertEquals(CQRS.literal(), args.get(17));
        Assertions.assertEquals("true", args.get(18));
        Assertions.assertEquals(PROJECTIONS.literal(), args.get(19));
        Assertions.assertEquals("true", args.get(20));
        Assertions.assertEquals(DATABASE.literal(), args.get(21));
        Assertions.assertEquals("None", args.get(22));
        Assertions.assertEquals(COMMAND_MODEL_DATABASE.literal(), args.get(23));
        Assertions.assertEquals("MariaDB", args.get(24));
        Assertions.assertEquals(QUERY_MODEL_DATABASE.literal(), args.get(25));
        Assertions.assertEquals("Postgres", args.get(26));
        Assertions.assertEquals(CLUSTER_NODES.literal(), args.get(27));
        Assertions.assertEquals("3", args.get(28));
        Assertions.assertEquals(DEPLOYMENT.literal(), args.get(29));
        Assertions.assertEquals("k8s", args.get(30));
        Assertions.assertEquals(DOCKER_IMAGE.literal(), args.get(31));
        Assertions.assertEquals("xoom-app", args.get(32));
        Assertions.assertEquals(KUBERNETES_IMAGE.literal(), args.get(33));
        Assertions.assertEquals("dambrosio/xoom-app", args.get(34));
        Assertions.assertEquals(KUBERNETES_POD_NAME.literal(), args.get(35));
        Assertions.assertEquals("xoom-app-pod", args.get(36));
        Assertions.assertEquals(TARGET_FOLDER.literal(), args.get(37));
        Assertions.assertEquals("/home/projects/xoom-app", args.get(38));
        Assertions.assertEquals(Agent.argumentKey(), args.get(39));
        Assertions.assertEquals(Agent.WEB.name(), args.get(40));
    }
}
