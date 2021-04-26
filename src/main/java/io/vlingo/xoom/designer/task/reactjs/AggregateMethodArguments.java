package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateMethodData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.RouteData;

public class AggregateMethodArguments implements TemplateArguments {

    public final AggregateMethodData method;
    public final AggregateData aggregate;
    public final RouteData route;

    public AggregateMethodArguments(AggregateMethodData method, AggregateData aggregate) {
        this.method = method;
        this.aggregate = aggregate;
        this.route = aggregate.api.routes.stream()
                .filter(routeData -> routeData.aggregateMethod.equals(method.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find " + method.name + "'s API data"));

    }
}
