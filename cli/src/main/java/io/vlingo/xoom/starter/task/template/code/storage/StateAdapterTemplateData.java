// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class StateAdapterTemplateData extends TemplateData {

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public static List<TemplateData> from(final String projectPath,
                                          final String persistencePackage,
                                          final StorageType storageType,
                                          final List<Content> contents) {
        return ContentQuery.findClassNames(AGGREGATE_PROTOCOL, contents)
                .stream().map(protocolName -> {
                    return new StateAdapterTemplateData(projectPath, protocolName,
                        persistencePackage, storageType, contents);
        }).collect(Collectors.toList());
    }

    public StateAdapterTemplateData(final String projectPath,
                                    final String protocolName,
                                    final String persistencePackage,
                                    final StorageType storageType,
                                    final List<Content> contents) {
        this.protocolName = protocolName;
        this.absolutePath = resolveAbsolutePath(persistencePackage, projectPath);
        this.templateParameters =
                loadParameters(persistencePackage, storageType, contents);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final StorageType storageType,
                                                  final List<Content> contents) {
        final String stateClassName = STATE.resolveClassname(protocolName);

        final String stateQualifiedClassName =
                ContentQuery.findFullyQualifiedClassName(STATE, stateClassName, contents);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                .and(IMPORTS, ImportParameter.of(stateQualifiedClassName))
                .and(STATE_NAME, stateClassName)
                .and(STATE_ADAPTER_NAME, STATE_ADAPTER.resolveClassname(protocolName))
                .and(STORAGE_TYPE, storageType);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

    @Override
    public File file() {
        return buildFile(STATE_ADAPTER, absolutePath, protocolName);
    }

}
