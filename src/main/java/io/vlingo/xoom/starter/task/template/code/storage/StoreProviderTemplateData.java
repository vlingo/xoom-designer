// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.code.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STORE_PROVIDER;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.*;

public class StoreProviderTemplateData extends TemplateData {

    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public static List<TemplateData> from(final String projectPath,
                                          final String persistencePackage,
                                          final boolean supportCQRS,
                                          final StorageType storageType,
                                          final ProjectionType projectionType,
                                          final Map<ModelClassification, DatabaseType> databaseTypes,
                                          final List<TemplateData> stateAdaptersTemplateData,
                                          final List<Content> contents) {
        final List<ModelClassification> modelClassifications =
                supportCQRS ? Arrays.asList(COMMAND, QUERY) : Arrays.asList(SINGLE);

        return modelClassifications.stream()
                .map(classification -> new StoreProviderTemplateData(
                        projectPath, persistencePackage, storageType,
                        databaseTypes.get(classification), projectionType,
                        classification, stateAdaptersTemplateData, contents
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
                storageType.actorFor(databaseType);

        final List<AdapterParameter> adapterParameters =
                AdapterParameter.from(stateAdaptersTemplateData);

        final List<String> sourceClassQualifiedNames =
                storageType.requireAdapters(modelClassification) ?
                storageType.adapterSourceClassStandards.stream()
                        .flatMap(standard ->
                            ContentQuery.findFullyQualifiedClassNames(standard, contents).stream()
                        ).collect(Collectors.toList()) :
                        Collections.emptyList();

        return CodeTemplateParameters.with(STORAGE_TYPE, storageType)
                .and(MODEL_CLASSIFICATION, modelClassification).and(DATABASE_TYPE, databaseType)
                .and(IMPORTS, ImportParameter.of(sourceClassQualifiedNames)).and(STORE_NAME, storeClassName)
                .and(PACKAGE_NAME, packageName).and(USE_PROJECTIONS, projectionType.isProjectionEnabled())
                .and(ADAPTERS, adapterParameters).and(CONNECTION_URL, databaseType.connectionUrl)
                .and(CONFIGURABLE, databaseType.configurable).and(PROJECTION_TYPE, projectionType)
                .andResolve(STORE_PROVIDER_NAME, params -> STORE_PROVIDER.resolveClassname(params))
                .enrich(params -> databaseType.addConfigurationParameters(params))
                .and(REQUIRE_ADAPTERS, storageType.requireAdapters(modelClassification));
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
