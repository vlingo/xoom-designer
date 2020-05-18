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
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static java.util.Arrays.asList;

public class StorageTemplateDataFactory {

    private final static String PACKAGE_PATTERN = "%s.%s.%s";
    private final static String PARENT_PACKAGE_NAME = "infrastructure";
    private final static String PERSISTENCE_PACKAGE_NAME = "persistence";

    public static Map<CodeTemplateStandard, List<TemplateData>> build(final String basePackage,
                                                                      final String projectPath,
                                                                      final boolean supportCQRS,
                                                                      final StorageType storageType,
                                                                      final DatabaseType databaseType,
                                                                      final List<Content> contents) {
        final String persistencePackage = resolvePackage(basePackage);
        final List<ImportParameter> importParameters = buildImports(contents);

        final List<TemplateData> stateAdapters =
                StateAdapterTemplateData.from(projectPath, persistencePackage,
                         storageType, contents, importParameters);

        final List<TemplateData> storeProviders =
                StoreProviderTemplateData.from(projectPath, persistencePackage,
                        supportCQRS, storageType, databaseType, importParameters,
                        stateAdapters);

        final TemplateData storeProviderConfiguration =
                StoreProviderConfigurationTemplateData.from(projectPath,
                        persistencePackage, storageType, storeProviders);

        return collect(stateAdapters, storeProviders, storeProviderConfiguration);
    }

    private static String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage,
                PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME).toLowerCase();
    }

    private static List<ImportParameter> buildImports(final List<Content> contents) {
        return ContentQuery.findFullyQualifiedClassNames(STATE, contents)
                .stream().map(qualifiedClassName -> new ImportParameter(qualifiedClassName))
                .collect(Collectors.toList());
    }

    private static Map<CodeTemplateStandard, List<TemplateData>> collect(final List<TemplateData> stateAdaptersTemplateData,
                                                                         final List<TemplateData> storeProvidersTemplateData,
                                                                         final TemplateData storeProviderConfigurationTemplateData) {
        return new LinkedHashMap<CodeTemplateStandard, List<TemplateData>>() {{
            put(STATE_ADAPTER, stateAdaptersTemplateData);
            put(STORE_PROVIDER, storeProvidersTemplateData);
            put(STORE_PROVIDER_CONFIGURATION, asList(storeProviderConfigurationTemplateData));
        }};
    }

}
