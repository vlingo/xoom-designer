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
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.PROJECTION_TO_DESCRIPTION;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.AGGREGATE_PROTOCOL;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.PROJECTION_DISPATCHER_PROVIDER;

public class ProjectionDispatcherProviderTemplateData extends TemplateData {

    private static final String PACKAGE_PATTERN = "%s.%s.%s";
    private static final String PARENT_PACKAGE_NAME = "infrastructure";
    private static final String PERSISTENCE_PACKAGE_NAME = "persistence";

    private final String absolutePath;
    private final CodeTemplateParameters templateParameters;

    public static ProjectionDispatcherProviderTemplateData from(final String basePackage,
                                                                final String projectPath,
                                                                final ProjectionType projectionType,
                                                                final List<Content> contents) {
        final List<String> aggregateProtocols = ContentQuery.findClassNames(AGGREGATE_PROTOCOL, contents);
        return new ProjectionDispatcherProviderTemplateData(basePackage, projectPath,
                projectionType, aggregateProtocols);
    }

    private ProjectionDispatcherProviderTemplateData(final String basePackage,
                                                     final String projectPath,
                                                     final ProjectionType projectionType,
                                                     final List<String> aggregateProtocols) {
        final String packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.templateParameters = loadParameters(packageName, projectionType, aggregateProtocols);
    }

    private CodeTemplateParameters loadParameters(final String packageName,
                                                  final ProjectionType projectionType,
                                                  final List<String> aggregateProtocols) {
        final List<ProjectToDescriptionParameter> projectToDescriptionParameters =
                ProjectToDescriptionParameter.from(projectionType, aggregateProtocols);

        return CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                .and(PROJECTION_TO_DESCRIPTION, projectToDescriptionParameters);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(PROJECTION_DISPATCHER_PROVIDER, templateParameters, absolutePath);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return templateParameters;
    }

}
