// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.projections;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.ENTITY_DATA;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;

public class EntityDataTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "infrastructure";

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public static EntityDataTemplateData from(final String basePackage,
                                              final String projectPath,
                                              final String protocolName,
                                              final List<Content> contents) {
        return new EntityDataTemplateData(basePackage, projectPath,
                protocolName, contents);
    }


    private EntityDataTemplateData(final String basePackage,
                                   final String projectPath,
                                   final String protocolName,
                                   final List<Content> contents) {
        final String packageName = resolvePackage(basePackage);
        this.protocolName = protocolName;
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.templateParameters = loadParameters(packageName, protocolName, contents);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final String protocolName,
                                                  final List<Content> contents) {
        final String stateName =
                STATE.resolveClassname(protocolName);

        final String stateQualifiedClassName =
                ContentQuery.findFullyQualifiedClassName(STATE, stateName, contents);

        final String dataName =
                ENTITY_DATA.resolveClassname(protocolName);

        final String entityDataQualifiedClassName = packageName.concat(".").concat(dataName);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                            .and(ENTITY_DATA_NAME, dataName)
                            .and(STATE_NAME, stateName)
                            .and(ENTITY_DATA_QUALIFIED_CLASS_NAME, entityDataQualifiedClassName)
                            .and(STATE_QUALIFIED_CLASS_NAME, stateQualifiedClassName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage,
                PARENT_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(absolutePath, protocolName);
    }

    @Override
    public CodeTemplateParameters parameters() {
        return templateParameters;
    }

    @Override
    public CodeTemplateStandard standard() {
        return ENTITY_DATA;
    }

}
