// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.projections;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import io.vlingo.xoom.starter.task.template.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class ProjectionTemplateDataFactory {

    public static Map<CodeTemplateStandard, List<TemplateData>> build(final String basePackage,
                                                                      final String projectPath,
                                                                      final ProjectionType projectionType,
                                                                      final List<Content> contents) {
        final List<String> aggregateProtocols =
                ContentQuery.findClassNames(AGGREGATE_PROTOCOL, contents);

        final Map<CodeTemplateStandard, List<TemplateData>> templatesData = new LinkedHashMap<>(3);

        loadTemplateData(basePackage, projectPath, contents,
                projectionType, aggregateProtocols, templatesData);

        return templatesData;
    }

    private static void loadTemplateData(final String basePackage,
                                         final String projectPath,
                                         final List<Content> contents,
                                         final ProjectionType projectionType,
                                         final List<String> aggregateProtocols,
                                         final Map<CodeTemplateStandard, List<TemplateData>> templatesData) {
        aggregateProtocols.stream().forEach(protocolName -> {
            final TemplateData entityData =
                    EntityDataTemplateData.from(basePackage, projectPath,
                            protocolName, contents);

            final TemplateData projectionData =
                    ProjectionTemplateData.from(basePackage, projectPath,
                            protocolName, contents, projectionType, entityData);

            final TemplateData providerData =
                    ProjectionDispatcherProviderTemplateData.from(basePackage,
                            projectPath, projectionType, contents);

            putTemplateData(ENTITY_DATA, entityData, list -> true, templatesData);
            putTemplateData(PROJECTION, projectionData, list -> true, templatesData);
            putTemplateData(PROJECTION_DISPATCHER_PROVIDER, providerData, list -> list.isEmpty(), templatesData);
        });
    }

    private static void putTemplateData(final CodeTemplateStandard codeTemplateStandard,
                                        final TemplateData templateData,
                                        final Predicate<List> condition,
                                        final Map<CodeTemplateStandard, List<TemplateData>> templatesData) {
        final List<TemplateData> data =
                templatesData.computeIfAbsent(codeTemplateStandard, key -> new ArrayList<>());

        if(condition.test(data)) {
            data.add(templateData);
        }
    }

}
