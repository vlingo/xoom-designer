// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.projections;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.ENTITY_DATA;

public class EntityDataTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "infrastructure";

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public EntityDataTemplateData from(final String basePackage,
                                       final String projectPath,
                                       final String protocolName,
                                       final String stateName,
                                       final List<ImportParameter> importParameters) {
        return new EntityDataTemplateData(basePackage, projectPath,
                protocolName, stateName, importParameters);
    }


    private EntityDataTemplateData(final String basePackage,
                                  final String projectPath,
                                  final String protocolName,
                                  final String stateName,
                                  final List<ImportParameter> importParameters) {
        final String packageName = resolvePackage(basePackage);
        this.protocolName = protocolName;
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.templateParameters = loadParameters(packageName, protocolName, stateName, importParameters);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final String aggregateProtocolName,
                                                  final String aggregateStateName,
                                                  final List<ImportParameter> importParameters) {
        final ImportParameter importParameter =
                ImportParameter.findImportParameter(aggregateStateName, importParameters);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                            .and(ENTITY_NAME, aggregateProtocolName)
                            .and(STATE_QUALIFIED_CLASS_NAME, importParameter.qualifiedClassName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage,
                PARENT_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(ENTITY_DATA, absolutePath, protocolName);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

}
