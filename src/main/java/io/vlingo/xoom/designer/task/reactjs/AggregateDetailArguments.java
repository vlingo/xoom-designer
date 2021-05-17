package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ValueObjectFieldData;

import java.util.List;
import java.util.Map;

public class AggregateDetailArguments {

    public final AggregateData aggregate;
    public final Map<String, List<ValueObjectFieldData>> valueTypes;

    public AggregateDetailArguments(AggregateData aggregate, Map<String, List<ValueObjectFieldData>> valueObjectData) {
        this.aggregate = aggregate;
        this.valueTypes = valueObjectData;
    }
}
