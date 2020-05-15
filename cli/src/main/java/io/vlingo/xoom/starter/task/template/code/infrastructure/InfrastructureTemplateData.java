// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static io.vlingo.xoom.starter.task.template.code.infrastructure.ModelClassification.*;

public class InfrastructureTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "infrastructure";
    private final static String STORE_PROVIDER_CONFIGURATION_NAME = "StoreProviderConfiguration";

    private final String packageName;
    private final String absolutePath;
    private final boolean supportCQRS;
    private final StorageType storageType;
    private final DatabaseType databaseType;
    private final List<ImportParameter> importParameters;
    private final CodeTemplateParameters templateParameters;
    public final List<TemplateData> stateAdapterTemplateData = new ArrayList<>();
    public final List<TemplateData> storeProvidersTemplateData = new ArrayList<>();

    public static InfrastructureTemplateData with(final String basePackage,
                                                  final String projectPath,
                                                  final boolean supportCQRS,
                                                  final StorageType storageType,
                                                  final DatabaseType databaseType,
                                                  final List<Content> contents) {
        return new InfrastructureTemplateData(basePackage, projectPath,
                supportCQRS, storageType, databaseType, contents);
    }

    private InfrastructureTemplateData(final String basePackage,
                                       final String projectPath,
                                       final boolean supportCQRS,
                                       final StorageType storageType,
                                       final DatabaseType databaseType,
                                       final List<Content> contents) {
        this.databaseType = databaseType;
        this.storageType = storageType;
        this.supportCQRS = supportCQRS;
        this.importParameters = buildImports(contents);
        this.packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(basePackage, projectPath, PARENT_PACKAGE_NAME);
        this.stateAdapterTemplateData.addAll(buildStateAdapters(contents));
        this.storeProvidersTemplateData.addAll(buildStoreProviders());
        this.templateParameters = loadParameters();
    }

    public Map<CodeTemplateStandard, List<TemplateData>> collectAll() {
        return new LinkedHashMap<CodeTemplateStandard, List<TemplateData>>() {{
            put(STORE_PROVIDER_CONFIGURATION, asList());
            put(STATE_ADAPTER, stateAdapterTemplateData);
            put(STORE_PROVIDER, storeProvidersTemplateData);
        }};
    }

    private List<TemplateData> buildStateAdapters(final List<Content> contents) {
        return ContentQuery.findClassNames(STATE, contents)
                .stream().map(stateClassName -> {
                    return new StateAdapterTemplateData(packageName, absolutePath,
                            storageType, stateClassName, importParameters);
                }).collect(Collectors.toList());
    }

    private List<TemplateData> buildStoreProviders() {
        final List<ModelClassification> modelClassifications =
                supportCQRS ? Arrays.asList(COMMAND, QUERY) : Arrays.asList(SINGLE);

        return modelClassifications.stream()
                .map(modelClassification -> new StoreProviderTemplateData(
                        packageName, absolutePath, storageType,
                        databaseType, modelClassification,
                        importParameters, stateAdapterTemplateData
                )).collect(Collectors.toList());
    }

    private List<ImportParameter> buildImports(final List<Content> contents) {
        return ContentQuery.findFullyQualifiedClassNames(STATE, contents)
                .stream().map(qualifiedClassName -> new ImportParameter(qualifiedClassName))
                .collect(Collectors.toList());
    }

    private CodeTemplateParameters loadParameters() {
        final List<StoreProviderParameter> providerParameters =
                StoreProviderParameter.from(storeProvidersTemplateData);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                .and(REGISTRY_CLASS_NAME, storageType.registryClassName)
                .and(REGISTRY_QUALIFIED_CLASS_NAME, storageType.registryQualifiedClassName())
                .and(STORE_PROVIDERS, providerParameters);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    private List<TemplateData> asList() {
        return Arrays.asList(this);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

    @Override
    public File file() {
        return new File(Paths.get(absolutePath, STORE_PROVIDER_CONFIGURATION_NAME + ".java").toString());
    }

}
