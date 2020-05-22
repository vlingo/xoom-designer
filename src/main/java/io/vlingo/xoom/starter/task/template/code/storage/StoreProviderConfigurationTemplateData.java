// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.PROJECTION_DISPATCHER_PROVIDER;

public class StoreProviderConfigurationTemplateData extends TemplateData {

    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public static StoreProviderConfigurationTemplateData from(final String projectPath,
                                                              final String persistencePackage,
                                                              final StorageType storageType,
                                                              final ProjectionType projectionType,
                                                              final List<TemplateData> storeProvidersTemplateData,
                                                              final List<Content> contents) {
        final List<StoreProviderParameter> providerParameters =
                StoreProviderParameter.from(storeProvidersTemplateData);

        return new StoreProviderConfigurationTemplateData(projectPath,
                persistencePackage, storageType, projectionType,
                providerParameters, contents);
    }

    private StoreProviderConfigurationTemplateData(final String projectPath,
                                                   final String infrastructurePackage,
                                                   final StorageType storageType,
                                                   final ProjectionType projectionType,
                                                   final List<StoreProviderParameter> providerParameters,
                                                   final List<Content> contents) {
        this.absolutePath = resolveAbsolutePath(infrastructurePackage, projectPath);
        this.parameters = loadParameters(infrastructurePackage, storageType, projectionType, providerParameters, contents);
    }

    private CodeTemplateParameters loadParameters(final String infrastructurePackage,
                                                  final StorageType storageType,
                                                  final ProjectionType projectionType,
                                                  final List<StoreProviderParameter> providerParameters,
                                                  final List<Content> contents) {
        final CodeTemplateParameters templateParameters =
                CodeTemplateParameters.with(PACKAGE_NAME, infrastructurePackage)
                        .and(REGISTRY_CLASS_NAME, storageType.registryClassName)
                        .and(REGISTRY_QUALIFIED_CLASS_NAME, storageType.registryQualifiedClassName())
                        .and(STORE_PROVIDERS, providerParameters)
                        .and(USE_PROJECTIONS, projectionType.isProjectionEnabled());

        if(!projectionType.isProjectionEnabled()) {
            return templateParameters;
        }

        final String projectionDispatcherProviderName =
                ContentQuery.findClassName(PROJECTION_DISPATCHER_PROVIDER, contents);

        final String projectionDispatcherProviderQualifiedName =
                ContentQuery.findFullyQualifiedClassName(PROJECTION_DISPATCHER_PROVIDER, contents);

        return templateParameters.and(PROJECTION_DISPATCHER_PROVIDER_NAME, projectionDispatcherProviderName)
                .and(PROJECTION_DISPATCHER_PROVIDER__QUALIFIED_CLASS_NAME, projectionDispatcherProviderQualifiedName);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return parameters;
    }

    @Override
    public File file() {
        return buildFile(CodeTemplateStandard.STORE_PROVIDER_CONFIGURATION, parameters, absolutePath);
    }

}
