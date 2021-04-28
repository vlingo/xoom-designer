package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;

import java.util.List;

public class SidebarArguments {

    public final List<AggregateData> aggregates;

    public SidebarArguments(List<AggregateData> aggregates) {
        this.aggregates = aggregates;
    }
}
