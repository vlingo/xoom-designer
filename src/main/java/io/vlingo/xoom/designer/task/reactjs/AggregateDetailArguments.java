package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.infrastructure.restapi.data.AggregateData;
import io.vlingo.xoom.designer.infrastructure.restapi.data.ValueObjectFieldData;
import io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.Aggregate;

import java.util.List;
import java.util.Map;

public class AggregateDetailArguments {

    public final Aggregate aggregate;
    public final Map<String, List<ValueObjectFieldData>> valueTypes;

    public AggregateDetailArguments(final Aggregate aggregate, Map<String, List<ValueObjectFieldData>> valueObjectData) {
        this.aggregate = aggregate;
        this.valueTypes = valueObjectData;
    }
}
