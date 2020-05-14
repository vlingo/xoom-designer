// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

public enum CodeTemplateParameter {

    PACKAGE_NAME("packageName"),
    AGGREGATE_PROTOCOL_NAME("aggregateProtocolName"),
    AGGREGATE_NAME("aggregateName"),
    DOMAIN_EVENT_NAME("domainEventName"),
    AGGREGATE_STATE_QUALIFIED_NAME("stateQualifiedName"),
    PLACEHOLDER_DEFINED_EVENT_NAME("placeholderDefinedEvent"),
    IMPORTS("imports"),
    STATE_ADAPTERS("stateAdapters"),
    STORE_PROVIDER_NAME("storeProviderName"),
    STORE_CLASS_NAME("storeClassName"),
    STATE_NAME("stateClass"),
    STORAGE_TYPE("storageType"),
    MODEL_CLASSIFICATION("modelClassification"),
    REGISTRY_CLASS_NAME("registryClassName"),
    REGISTRY_QUALIFIED_CLASS_NAME("registryQualifiedClassName"),
    STORE_PROVIDERS("storeProviders"),
    CONNECTION_URL("connectionUrl"),
    DRIVER_CLASS("driverClass");

    public final String token;

    CodeTemplateParameter(String token) {
        this.token = token;
    }

}
