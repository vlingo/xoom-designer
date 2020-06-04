// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.ADAPTER;

public class AdapterTemplateData extends TemplateData {

    private final String absolutePath;
    private final String sourceClassName;
    private final CodeTemplateStandard sourceClassStandard;
    private final CodeTemplateParameters templateParameters;

    public static List<TemplateData> from(final String projectPath,
                                          final String persistencePackage,
                                          final StorageType storageType,
                                          final List<Content> contents) {
        return ContentQuery.findClassNames(storageType.adapterSourceClassStandard, contents)
                    .stream().map(sourceClassName ->
                        new AdapterTemplateData(projectPath, sourceClassName,
                                storageType.adapterSourceClassStandard,
                                persistencePackage, storageType, contents)
                    ).collect(Collectors.toList());
    }

    public AdapterTemplateData(final String projectPath,
                               final String sourceClassName,
                               final CodeTemplateStandard sourceClassStandard,
                               final String persistencePackage,
                               final StorageType storageType,
                               final List<Content> contents) {
        this.sourceClassName = sourceClassName;
        this.sourceClassStandard = sourceClassStandard;
        this.absolutePath = resolveAbsolutePath(persistencePackage, projectPath);
        this.templateParameters = loadParameters(persistencePackage, storageType, contents);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final StorageType storageType,
                                                  final List<Content> contents) {
        final String sourceQualifiedClassName =
                ContentQuery.findFullyQualifiedClassName(sourceClassStandard, sourceClassName, contents);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                .and(IMPORTS, ImportParameter.of(sourceQualifiedClassName))
                .and(SOURCE_NAME, sourceClassName)
                .and(ADAPTER_NAME, ADAPTER.resolveClassname(sourceClassName))
                .and(STORAGE_TYPE, storageType);
    }

    @Override
    public CodeTemplateParameters parameters() {
        return templateParameters;
    }

    @Override
    public File file() {
        return buildFile(absolutePath, sourceClassName);
    }

    @Override
    public CodeTemplateStandard standard() {
        return ADAPTER;
    }

}
