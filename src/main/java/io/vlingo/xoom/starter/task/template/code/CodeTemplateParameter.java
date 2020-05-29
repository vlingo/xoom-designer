// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

public enum CodeTemplateParameter {

    PACKAGE_NAME("packageName"),
    APPLICATION_NAME("appName"),
    AGGREGATE_PROTOCOL_NAME("aggregateProtocolName"),
    AGGREGATE_NAME("aggregateName"),
    DOMAIN_EVENT_NAME("domainEventName"),
    ENTITY_NAME("entityName"),
    ENTITY_DATA_NAME("dataName"),
    ENTITY_DATA_QUALIFIED_CLASS_NAME("dataQualifiedName"),
    AGGREGATE_STATE_QUALIFIED_NAME("stateQualifiedName"),
    IMPORTS("imports"),
    CONFIGURABLE("configurable"),
    DATABASE_TYPE("databaseType"),
    STATE_ADAPTERS("stateAdapters"),
    STORE_PROVIDER_NAME("storeProviderName"),
    STORE_NAME("storeClassName"),
    STATE_NAME("stateName"),
    STATE_ADAPTER_NAME("stateAdapterName"),
    STATE_QUALIFIED_CLASS_NAME("stateQualifiedClassName"),
    STORAGE_TYPE("storageType"),
    STORAGE_DELEGATE_NAME("storageDelegateName"),
    PROJECTION_NAME("projectionName"),
    PROJECTION_TYPE("projectionType"),
    PROJECTION_TO_DESCRIPTION("projectToDescriptions"),
    USE_PROJECTIONS("useProjections"),
    MODEL_CLASSIFICATION("modelClassification"),
    REST_RESOURCE_NAME("resourceName"),
    REST_RESOURCES("restResources"),
    REGISTRY_CLASS_NAME("registryClassName"),
    REGISTRY_QUALIFIED_CLASS_NAME("registryQualifiedClassName"),
    CONFIGURATION_PROVIDER_NAME("configurationProviderName"),
    PROJECTION_DISPATCHER_PROVIDER_NAME("projectionDispatcherProviderName"),
    PROJECTION_DISPATCHER_PROVIDER_QUALIFIED_CLASS_NAME("projectionDispatcherProviderQualifiedClassName"),
    PROVIDERS("providers"),
    CONNECTION_URL("connectionUrl");

    public final String key;

    CodeTemplateParameter(String key) {
        this.key = key;
    }

}
