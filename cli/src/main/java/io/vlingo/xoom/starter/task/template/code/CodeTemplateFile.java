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
    STATE_STORE_PROVIDER("StateStoreProvider"),
    OBJECT_STORE_PROVIDER("ObjectStoreProvider"),
    JOURNAL_PROVIDER("JournalProvider"),
    QUERY_MODEL_STATE_STORE_PROVIDER("QueryModelStateStoreProvider"),
    QUERY_MODEL_OBJECT_STORE_PROVIDER("QueryModelObjectStoreProvider"),
    QUERY_MODEL_JOURNAL_PROVIDER("QueryModelJournalProvider"),
    STORE_PROVIDER_CONFIGURATION("StoreProviderConfiguration"),
    STORAGE_CONFIGURATION("StorageConfiguration"),
    PLACEHOLDER_DOMAIN_EVENT("PlaceholderDefinedEvent"),
    STATE_ADAPTER("StateAdapter"),
    REST_RESOURCE("RestResource");

    public final String filename;

    CodeTemplateFile(final String filename) {
        this.filename = filename;
    }

}
