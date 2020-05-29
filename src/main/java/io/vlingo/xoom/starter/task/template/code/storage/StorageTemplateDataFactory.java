// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.DatabaseType;
import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static java.util.Arrays.asList;

public class StorageTemplateDataFactory {

    private final static String PACKAGE_PATTERN = "%s.%s.%s";
    private final static String PARENT_PACKAGE_NAME = "infrastructure";
    private final static String PERSISTENCE_PACKAGE_NAME = "persistence";

    public static Map<CodeTemplateStandard, List<TemplateData>> build(final String basePackage,
                                                                      final String projectPath,
                                                                      final boolean supportCQRS,
                                                                      final List<Content> contents,
                                                                      final StorageType storageType,
                                                                      final Map<ModelClassification, DatabaseType> databaseTypes,
                                                                      final ProjectionType projectionType) {
        final String persistencePackage =
                resolvePackage(basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME);

        final List<TemplateData> stateAdaptersTemplateData =
                StateAdapterTemplateData.from(projectPath, persistencePackage,
                        storageType, contents);

        final List<TemplateData> storeProvidersTemplateData =
                StoreProviderTemplateData.from(projectPath, persistencePackage,
                        supportCQRS, storageType, projectionType, databaseTypes,
                        stateAdaptersTemplateData, contents);

        return new LinkedHashMap<CodeTemplateStandard, List<TemplateData>>() {{
            put(STATE_ADAPTER, stateAdaptersTemplateData);
            put(STORE_PROVIDER, storeProvidersTemplateData);
        }};
    }

    private static String resolvePackage(final String... additionalPackages) {
        return String.format(PACKAGE_PATTERN, additionalPackages).toLowerCase();
    }

}
