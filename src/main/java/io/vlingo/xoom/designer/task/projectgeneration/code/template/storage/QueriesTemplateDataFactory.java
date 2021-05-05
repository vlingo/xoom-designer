// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.parameter.ImportParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_PROTOCOL;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class QueriesTemplateDataFactory {

  public static List<TemplateData> from(final String persistencePackage,
                                        final Boolean useCQRS,
                                        final List<Content> contents) {
    if (!useCQRS) {
      return Collections.emptyList();
    }
    return ContentQuery.findClassNames(AGGREGATE_PROTOCOL, contents)
            .stream().map(protocol -> createTemplates(protocol, persistencePackage, contents))
            .flatMap(templateData -> templateData.stream()).collect(Collectors.toList());
  }

  private static List<TemplateData> createTemplates(final String protocol,
                                                    final String persistencePackage,
                                                    final List<Content> contents) {
    final TemplateParameters parameters =
            createParameters(persistencePackage, protocol, contents);

    return Arrays.asList(new QueriesTemplateData(protocol, parameters),
            new QueriesActorTemplateData(protocol, parameters));
  }

  private static TemplateParameters createParameters(final String persistencePackage,
                                                     final String aggregateProtocol,
                                                     final List<Content> contents) {
    final String dataObjectName =
            DATA_OBJECT.resolveClassname(aggregateProtocol);

    final String dataObjectQualifiedName =
            ContentQuery.findFullyQualifiedClassName(DATA_OBJECT, dataObjectName, contents);

    return TemplateParameters.with(PACKAGE_NAME, persistencePackage)
            .and(STATE_DATA_OBJECT_NAME, dataObjectName)
            .and(QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregateProtocol))
            .and(QUERY_ALL_METHOD_NAME, QueriesDetail.resolveQueryAllMethodName(aggregateProtocol))
            .and(IMPORTS, ImportParameter.of(dataObjectQualifiedName));
  }

}