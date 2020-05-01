package io.vlingo.xoom.starter.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class AggregateData {

    public final String name;
    public final List<DomainEventData> domainEvents = new ArrayList<>();

    public AggregateData(final String name) {
        this.name = name;
    }

}
