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
    PLACEHOLDER_EVENT("placeholderEvent"),
    ENTITY_NAME("entityName"),
    ENTITY_DATA_NAME("dataName"),
    ENTITY_DATA_QUALIFIED_CLASS_NAME("dataQualifiedName"),
    AGGREGATE_STATE_QUALIFIED_NAME("stateQualifiedName"),
    IMPORTS("imports"),
    CONFIGURABLE("configurable"),
    DATABASE_TYPE("databaseType"),
    ADAPTERS("adapters"),
    STORE_PROVIDER_NAME("storeProviderName"),
    STORE_NAME("storeClassName"),
    STATE_NAME("stateName"),
    SOURCE_NAME("sourceName"),
    ADAPTER_NAME("adapterName"),
    STATE_QUALIFIED_CLASS_NAME("stateQualifiedClassName"),
    STORAGE_TYPE("storageType"),
    REQUIRE_ADAPTERS("requireAdapters"),
    STORAGE_DELEGATE_NAME("storageDelegateName"),
    PROJECTION_NAME("projectionName"),
    PROJECTION_TYPE("projectionType"),
    PROJECTION_TO_DESCRIPTION("projectToDescriptions"),
    USE_PROJECTIONS("useProjections"),
    USE_ANNOTATIONS("useAnnotations"),
    MODEL_CLASSIFICATION("modelClassification"),
    STORE_ATTRIBUTE_NAME("storeAttributeName"),
    REST_RESOURCE_NAME("resourceName"),
    REST_RESOURCE("restResource"),
    REST_RESOURCES("restResources"),
    REGISTRY_QUALIFIED_CLASS_NAME("registryQualifiedClassName"),
    TYPE_REGISTRIES("registries"),
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
