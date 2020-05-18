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
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class ProjectionTemplateData extends TemplateData {

    private static final String PACKAGE_PATTERN = "%s.%s.%s";
    private static final String PARENT_PACKAGE_NAME = "infrastructure";
    private static final String PERSISTENCE_PACKAGE_NAME = "persistence";

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    private ProjectionTemplateData (final String basePackage,
                                    final String projectPath,
                                    final String protocolName,
                                    final List<Content> contents) {
        final String packageName =  resolvePackage(basePackage);
        this.protocolName = protocolName;
        this.templateParameters = loadParameters(packageName, protocolName, null);
        this.absolutePath =
                resolveAbsolutePath(basePackage, projectPath, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME);
    }


    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final String protocolName,
                                                  final List<Content> contents) {
        final String stateName = STATE.resolveClassname(protocolName);
        final String projectionName = PROJECTION.resolveClassname(protocolName);
        final String entityDataName = ENTITY_DATA.resolveClassname(protocolName);
        final List<ImportParameter> imports = resolveImports(stateName, entityDataName, contents);
        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                .and(IMPORTS, imports)
                .and(PROJECTION_NAME, projectionName)
                .and(STATE_NAME, stateName)
                .and(ENTITY_NAME, entityDataName);
    }

    private List<ImportParameter> resolveImports(final String stateName,
                                                 final String entityDataName,
                                                 final List<Content> contents) {
        final String stateQualifiedName =
                ContentQuery.findFullyQualifiedClassNames(STATE, stateName, contents);

        final String entityDataQualifiedName =
                ContentQuery.findFullyQualifiedClassNames(ENTITY_DATA, entityDataName, contents);

        return ImportParameter.of(stateQualifiedName, entityDataQualifiedName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME,
                PERSISTENCE_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(CodeTemplateStandard.PROJECTION, absolutePath, protocolName);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

}
