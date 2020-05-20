// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.ProjectionType;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STORE_PROVIDER;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.*;

public class StoreProviderTemplateData extends TemplateData {

    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public static List<TemplateData> from(final String projectPath,
                                          final String persistencePackage,
                                          final boolean supportCQRS,
                                          final StorageType storageType,
                                          final DatabaseType databaseType,
                                          final ProjectionType projectionType,
                                          final List<TemplateData> stateAdaptersTemplateData,
                                          final List<Content> contents) {
        final List<ModelClassification> modelClassifications =
                supportCQRS ? Arrays.asList(COMMAND, QUERY) : Arrays.asList(SINGLE);

        return modelClassifications.stream()
                .map(classification -> new StoreProviderTemplateData(
                        projectPath, persistencePackage, storageType,
                        databaseType, projectionType, classification,
                        stateAdaptersTemplateData, contents
                )).collect(Collectors.toList());
    }

    private StoreProviderTemplateData(final String projectPath,
                                      final String persistencePackage,
                                      final StorageType storageType,
                                      final DatabaseType databaseType,
                                      final ProjectionType projectionType,
                                      final ModelClassification modelClassification,
                                      final List<TemplateData> stateAdaptersTemplateData,
                                      final List<Content> contents) {
        this.absolutePath = resolveAbsolutePath(persistencePackage, projectPath);
        this.templateParameters =
                loadParameters(persistencePackage, storageType, databaseType, projectionType,
                        modelClassification, stateAdaptersTemplateData, contents);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final StorageType storageType,
                                                  final DatabaseType databaseType,
                                                  final ProjectionType projectionType,
                                                  final ModelClassification modelClassification,
                                                  final List<TemplateData> stateAdaptersTemplateData,
                                                  final List<Content> contents) {
        final String storeClassName =
                StoreInformation.qualifiedNameFor(databaseType, storageType);

        final List<StateAdapterParameter> stateAdapterParameters =
                StateAdapterParameter.from(stateAdaptersTemplateData);

        final List<String> stateQualifiedNames =
                ContentQuery.findFullyQualifiedClassNames(STATE, contents);

        final CodeTemplateParameters parameters =
                CodeTemplateParameters.with(STORAGE_TYPE, storageType)
                        .and(MODEL_CLASSIFICATION, modelClassification);

        final String providerName = STORE_PROVIDER.resolveClassname(parameters);

        return parameters.and(IMPORTS, ImportParameter.of(stateQualifiedNames))
                .and(PACKAGE_NAME, packageName)
                .and(STORE_PROVIDER_NAME, providerName)
                .and(STORE_CLASS_NAME, storeClassName)
                .and(STATE_ADAPTERS, stateAdapterParameters)
                .and(PROJECTION_TYPE, projectionType)
                .and(USE_PROJECTIONS, projectionType.isProjectionEnabled())
                .and(CONNECTION_URL, databaseType.connectionUrl)
                .and(DRIVER_CLASS, databaseType.driverClass);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

    @Override
    public File file() {
        return buildFile(STORE_PROVIDER, templateParameters, absolutePath);
    }
}
