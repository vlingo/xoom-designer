// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;

public class StateAdapterTemplateData extends TemplateData {

    private static final String STATE_ADAPTER_SUFFIX = "Adapter";

    public final String adapterName;
    public final String stateClassName;
    public final CodeTemplateParameters templateParameters;
    public final File file;

    public static List<TemplateData> from(final String projectPath,
                                          final String infrastructurePackage,
                                          final StorageType storageType,
                                          final List<Content> contents,
                                          final List<ImportParameter> importParameters) {
        return ContentQuery.findClassNames(STATE, contents)
                .stream().map(stateClassName -> {
                    return new StateAdapterTemplateData(projectPath, infrastructurePackage,
                            storageType, stateClassName, importParameters);
        }).collect(Collectors.toList());
    }

    public StateAdapterTemplateData(final String projectPath,
                                    final String infrastructurePackage,
                                    final StorageType storageType,
                                    final String stateClassName,
                                    final List<ImportParameter> importParameters) {
        this.stateClassName = stateClassName;
        this.adapterName = stateClassName + STATE_ADAPTER_SUFFIX;
        this.templateParameters = loadParameters(infrastructurePackage, storageType, importParameters);
        this.file = buildFile(infrastructurePackage, projectPath);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final StorageType storageType,
                                                  final List<ImportParameter> importParameters) {
        final ImportParameter stateClassImport =
                ImportParameter.findImportParameter(stateClassName, importParameters);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                                     .and(IMPORTS, Arrays.asList(stateClassImport))
                                     .and(STATE_NAME, stateClassName)
                                     .and(STORAGE_TYPE, storageType);
    }

    private File buildFile(final String infrastructurePackage, final String projectPath) {
        final String absolutePath = resolveAbsolutePath(infrastructurePackage, projectPath);
        return new File(Paths.get(absolutePath, adapterName + ".java").toString());
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
