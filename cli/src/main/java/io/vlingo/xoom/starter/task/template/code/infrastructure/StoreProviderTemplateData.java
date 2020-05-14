// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public class StoreProviderTemplateData extends TemplateData {

    private static final String PROVIDER_NAME_SUFFIX = "Provider";

    public final File file;
    public final String providerName;
    public final CodeTemplateParameters templateParameters;

    public StoreProviderTemplateData(final String packageName,
                                     final String absolutePath,
                                     final StorageType storageType,
                                     final DatabaseType databaseType,
                                     final ModelClassification modelClassification,
                                     final List<ImportParameter> importParameters,
                                     final List<TemplateData> stateAdaptersTemplateData) {
        this.providerName = resolveStoreProviderName(storageType, modelClassification);
        this.file = buildFile(absolutePath);
        this.templateParameters =
                loadParameters(packageName, storageType, databaseType, modelClassification,
                        importParameters, stateAdaptersTemplateData);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final StorageType storageType,
                                                  final DatabaseType databaseType,
                                                  final ModelClassification modelClassification,
                                                  final List<ImportParameter> importParameters,
                                                  final List<TemplateData> stateAdaptersTemplateData) {
        final String storeClassName =
                StoreInformation.qualifiedNameFor(databaseType, storageType);

        final List<StateAdapterParameter> stateAdapterParameters =
                StateAdapterParameter.from(stateAdaptersTemplateData);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                        .and(IMPORTS, importParameters)
                        .and(MODEL_CLASSIFICATION, modelClassification)
                        .and(STORE_PROVIDER_NAME, providerName)
                        .and(STORE_CLASS_NAME, storeClassName)
                        .and(STATE_ADAPTERS, stateAdapterParameters)
                        .and(STORAGE_TYPE, storageType)
                        .and(CONNECTION_URL, databaseType.connectionUrl)
                        .and(DRIVER_CLASS, databaseType.driverClass);
    }

    private String resolveStoreProviderName(final StorageType storageType, final ModelClassification modelClassification) {
        if(modelClassification.isSingle())  {
            return storageType.title + PROVIDER_NAME_SUFFIX;
        }
        return modelClassification.title + storageType.title + PROVIDER_NAME_SUFFIX;
    }

    private File buildFile(final String absolutePath) {
        return new File(Paths.get(absolutePath, providerName + ".java").toString());
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

    @Override
    public File file() {
        return file;
    }
}
