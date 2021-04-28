package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;

import java.util.List;

public class AppArguments {

    public final List<AggregateData> aggregates;

    public AppArguments(List<AggregateData> aggregates) {
        this.aggregates = aggregates;
    }
}
