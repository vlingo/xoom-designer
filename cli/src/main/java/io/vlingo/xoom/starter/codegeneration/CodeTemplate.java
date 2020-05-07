package io.vlingo.xoom.starter.codegeneration;

public enum CodeTemplate {

    AGGREGATE_PROTOCOL("AggregateProtocol"),
    OBJECT_ENTITY("ObjectEntity"),
    STATEFUL_ENTITY("StatefulEntity"),
    EVENT_SOURCE_ENTITY("EventSourcedEntity"),
    STATE_OBJECT("BeanState"),
    PLAIN_STATE("ValueState"),
    DOMAIN_EVENT("DomainEvent"),
    PLACEHOLDER_DOMAIN_EVENT("PlaceholderDefinedEvent"),
    REST_RESOURCE("RestResource");

    public final String filename;

    CodeTemplate(final String filename) {
        this.filename = filename;
    }

}
