// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.queries;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.QueriesDetail;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class QueriesUnitTestTemplateData extends TemplateData {

  private final String queriesProtocolName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<Content> contents,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects) {
    final String packageName =
            ContentQuery.findPackage(JavaTemplateStandard.QUERIES, contents);

    final Function<CodeGenerationParameter, TemplateData> mapper =
            aggregate -> new QueriesUnitTestTemplateData(packageName, aggregate, contents, valueObjects);

    return aggregates.stream().map(mapper).collect(Collectors.toList());
  }

  public QueriesUnitTestTemplateData(final String packageName,
                                     final CodeGenerationParameter aggregate,
                                     final List<Content> contents,
                                     final List<CodeGenerationParameter> valueObjects) {
    this.queriesProtocolName =
            JavaTemplateStandard.QUERIES.resolveClassname(aggregate.value);

    final String dataObjectName =
            JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName)
                    .and(QUERIES_UNIT_TEST_NAME, standard().resolveClassname(queriesProtocolName))
                    .and(QUERIES_ACTOR_NAME, JavaTemplateStandard.QUERIES_ACTOR.resolveClassname(aggregate.value))
                    .and(QUERIES_NAME, queriesProtocolName).and(DATA_OBJECT_NAME, dataObjectName)
                    .and(QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregate.value))
                    .and(TEST_CASES, TestCase.from(aggregate, valueObjects))
                    .addImport(resolveImport(dataObjectName, contents))
                    .addImports(AggregateDetail.resolveImports(aggregate))
                    .and(PRODUCTION_CODE, false)
                    .and(UNIT_TEST, true);
  }

  private String resolveImport(final String dataObjectName,
                               final List<Content> contents) {
    final String dataObjectPackage =
            ContentQuery.findPackage(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents);

    final CodeElementFormatter codeElementFormatter =
            ComponentRegistry.withName("defaultCodeFormatter");

    return codeElementFormatter.importAllFrom(dataObjectPackage);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.QUERIES_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(QUERIES_UNIT_TEST_NAME);
  }

}
