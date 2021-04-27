package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateMethodData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.RouteData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ValueObjectFieldData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateMethodArguments implements TemplateArguments {

    public final AggregateMethodData method;
    public final AggregateData aggregate;
    public final RouteData route;
    public final Map<String, List<ValueObjectFieldData>> valueTypes;
    public final Map<String, String> fieldTypes;

    public AggregateMethodArguments(AggregateMethodData method, AggregateData aggregate, Map<String, List<ValueObjectFieldData>> valueObjectData) {
        this.method = method;
        this.aggregate = aggregate;
        this.valueTypes = valueObjectData;
        this.route = aggregate.api.routes.stream()
                .filter(routeData -> routeData.aggregateMethod.equals(method.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find " + method.name + "'s API data"));

        fieldTypes = aggregate.stateFields.stream()
                .collect(Collectors.toMap(
                        stateFieldData -> stateFieldData.name,
                        stateFieldData -> stateFieldData.type,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
