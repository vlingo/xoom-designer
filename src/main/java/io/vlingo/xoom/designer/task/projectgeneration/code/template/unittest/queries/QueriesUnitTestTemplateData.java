// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.queries;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.QueriesDetail;
import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class QueriesUnitTestTemplateData extends TemplateData {

  private final String queriesProtocolName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<Content> contents,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects) {
    final String packageName =
            ContentQuery.findPackage(DesignerTemplateStandard.QUERIES, contents);

    final Function<CodeGenerationParameter, TemplateData> mapper =
            aggregate -> new QueriesUnitTestTemplateData(packageName, aggregate, contents, valueObjects);

    return aggregates.stream().map(mapper).collect(Collectors.toList());
  }

  public QueriesUnitTestTemplateData(final String packageName,
                                     final CodeGenerationParameter aggregate,
                                     final List<Content> contents,
                                     final List<CodeGenerationParameter> valueObjects) {
    this.queriesProtocolName =
            DesignerTemplateStandard.QUERIES.resolveClassname(aggregate.value);

    final String dataObjectName =
            DATA_OBJECT.resolveClassname(aggregate.value);

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName)
                    .and(QUERIES_UNIT_TEST_NAME, standard().resolveClassname(queriesProtocolName))
                    .and(QUERIES_ACTOR_NAME, QUERIES_ACTOR.resolveClassname(aggregate.value))
                    .and(QUERIES_NAME, queriesProtocolName).and(DATA_OBJECT_NAME, dataObjectName)
                    .and(QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregate.value))
                    .and(TEST_CASES, TestCase.from(aggregate, valueObjects))
                    .addImport(resolveImport(dataObjectName, contents))
                    .and(PRODUCTION_CODE, false)
                    .and(UNIT_TEST, true);
  }

  private String resolveImport(final String dataObjectName,
                               final List<Content> contents) {
    final String dataObjectPackage =
            ContentQuery.findPackage(DATA_OBJECT, dataObjectName, contents);

    return CodeElementFormatter.importAllFrom(dataObjectPackage);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return QUERIES_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(QUERIES_UNIT_TEST_NAME);
  }

}
