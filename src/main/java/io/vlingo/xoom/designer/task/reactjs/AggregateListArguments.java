package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateListArguments implements TemplateArguments {

    public final AggregateData aggregate;
    public final AggregateMethodData creatorMethod;
    public final RouteData creatorRoute;
    public final Map<String, List<ValueObjectFieldData>> valueTypes;
    public final Map<String, String> fieldTypes;

    public AggregateListArguments(AggregateData aggregate, Map<String, List<ValueObjectFieldData>> valueObjectData) {
        this.aggregate = aggregate;
        this.valueTypes = valueObjectData;

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

        fieldTypes = aggregate.stateFields.stream()
                .collect(Collectors.toMap(
                        stateFieldData -> stateFieldData.name,
                        stateFieldData -> stateFieldData.type,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
