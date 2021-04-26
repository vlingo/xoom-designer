package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;

public class AggregateDetailArguments implements TemplateArguments {

    public final AggregateData aggregate;

    public AggregateDetailArguments(AggregateData aggregate) {
        this.aggregate = aggregate;
    }
}
