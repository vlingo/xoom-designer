package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateMethodData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.RouteData;

public class AggregateListArguments implements TemplateArguments {

    public final AggregateData aggregate;
    public final AggregateMethodData creatorMethod;
    public final RouteData creatorRoute;

    public AggregateListArguments(AggregateData aggregate) {
        this.aggregate = aggregate;

        final String creatorMethodName = aggregate.api.routes.stream()
                .filter(a -> !a.requireEntityLoad)
                .map(routeData -> routeData.aggregateMethod)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find creator method name"));
        creatorMethod = aggregate.methods.stream()
                .filter(a -> a.name.equals(creatorMethodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find creator method"));

        creatorRoute = aggregate.api.routes.stream()
                .filter(routeData -> !routeData.requireEntityLoad)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find creator route"));
    }
}
