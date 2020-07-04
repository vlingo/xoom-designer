// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.code.*;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;

public class BootstrapTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String INFRA_PACKAGE_NAME = "infrastructure";
    private static final String RESOURCES_ANNOTATION_QUALIFIED_NAME = "io.vlingo.xoom.annotation.initializer.Resources";

    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    private BootstrapTemplateData(final String basePackage,
                                  final String projectPath,
                                  final String artifactId,
                                  final StorageType storageType,
                                  final Boolean useCQRS,
                                  final Boolean useProjections,
                                  final List<Content> contents) {
        this.absolutePath =
                resolveAbsolutePath(basePackage, projectPath, INFRA_PACKAGE_NAME);

        this.parameters =
                loadParameters(basePackage, storageType, artifactId,
                        useCQRS, useProjections, contents);
    }

    public static BootstrapTemplateData from(final String basePackage,
                                             final String projectPath,
                                             final String artifactId,
                                             final StorageType storageType,
                                             final Boolean useCQRS,
                                             final Boolean useProjections,
                                             final List<Content> contents) {
        return new BootstrapTemplateData(basePackage, projectPath, artifactId,
                storageType, useCQRS, useProjections, contents);
    }

    private CodeTemplateParameters loadParameters(final String basePackage,
                                                  final StorageType storageType,
                                                  final String artifactId,
                                                  final Boolean useCQRS,
                                                  final Boolean useProjections,
                                                  final List<Content> contents) {
        final String packageName = resolvePackage(basePackage);



        final List<ImportParameter> imports = loadImports(storageType, contents, useCQRS);

        final RestResourceParameter restResourceParameters =
                RestResourceParameter.from(contents);

        final List<TypeRegistryParameter> typeRegistryParameters =
                TypeRegistryParameter.from(storageType, useCQRS);

        final List<ProviderParameter> providerParameters =
                ProviderParameter.from(storageType, useCQRS, useProjections);

        return CodeTemplateParameters.with(IMPORTS, imports)
                .and(PACKAGE_NAME, packageName)
                .and(APPLICATION_NAME, artifactId)
                .and(PROVIDERS, providerParameters)
                .and(USE_PROJECTIONS, useProjections)
                .and(CodeTemplateParameter.REST_RESOURCE, restResourceParameters)
                .and(TYPE_REGISTRIES, typeRegistryParameters)
                .andResolve(PROJECTION_DISPATCHER_PROVIDER_NAME,
                        param -> PROJECTION_DISPATCHER_PROVIDER.resolveClassname(param));
    }

    private List<ImportParameter> loadImports(final StorageType storageType,
                                              final List<Content> contents,
                                              final Boolean useCQRS) {

        final List<String> otherFullyQualifiedNames =
                ContentQuery.findFullyQualifiedClassNames(contents, REST_RESOURCE,
                        STORE_PROVIDER, PROJECTION_DISPATCHER_PROVIDER);

        if(ContentQuery.exists(REST_RESOURCE, contents)) {
            otherFullyQualifiedNames.add(RESOURCES_ANNOTATION_QUALIFIED_NAME);
        }

        final List<String> typeRegistriesFullyQualifiedNames =
                storageType.resolveTypeRegistryQualifiedNames(useCQRS);

        return ImportParameter.of(otherFullyQualifiedNames, typeRegistriesFullyQualifiedNames);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, INFRA_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(absolutePath);
    }

    @Override
    public CodeTemplateParameters parameters() {
        return parameters;
    }

    @Override
    public CodeTemplateStandard standard() {
        return BOOTSTRAP;
    }

}
