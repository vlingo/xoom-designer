// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

public enum CodeTemplateFile {

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

    CodeTemplateFile(final String filename) {
        this.filename = filename;
    }

}
