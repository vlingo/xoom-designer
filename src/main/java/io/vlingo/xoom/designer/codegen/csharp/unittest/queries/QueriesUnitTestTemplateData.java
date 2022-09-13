// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.QueriesDetail;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueriesUnitTestTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(String basePackage, final List<Content> contents, final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects) {

    final Function<CodeGenerationParameter, TemplateData> mapper =
        aggregate -> new QueriesUnitTestTemplateData(basePackage, aggregate, contents, valueObjects);

    return aggregates.stream().map(mapper).collect(Collectors.toList());
  }

  public QueriesUnitTestTemplateData(final String basePackage, final CodeGenerationParameter aggregate,
                                     final List<Content> contents, final List<CodeGenerationParameter> valueObjects) {
    final String queriesProtocolName = CsharpTemplateStandard.QUERIES.resolveClassname(aggregate.value);

    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, resolvePackage(basePackage))
        .and(TemplateParameter.QUERIES_UNIT_TEST_NAME, standard().resolveClassname(queriesProtocolName))
        .and(TemplateParameter.QUERIES_ACTOR_NAME, CsharpTemplateStandard.QUERIES_ACTOR.resolveClassname(aggregate.value))
        .and(TemplateParameter.QUERIES_NAME, String.format("I%s", queriesProtocolName)).and(TemplateParameter.DATA_OBJECT_NAME, dataObjectName)
        .and(TemplateParameter.QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregate.value))
        .and(TemplateParameter.TEST_CASES, TestCase.from(aggregate, valueObjects))
        .addImports(resolveImport(dataObjectName, aggregate, contents))
        .and(TemplateParameter.PRODUCTION_CODE, false)
        .and(TemplateParameter.UNIT_TEST, true);
  }

  private static String resolvePackage(final String basePackage) {
    return basePackage + ".Tests.Infrastructure.Persistence";
  }

  private Set<String> resolveImport(final String dataObjectName, final CodeGenerationParameter aggregate,
                                    final List<Content> contents) {
    final Set<String> imports = AggregateDetail.resolveImports(aggregate);

    imports.add(ContentQuery.findPackage(CsharpTemplateStandard.DATA_OBJECT, dataObjectName, contents));
    imports.add(ContentQuery.findPackage(CsharpTemplateStandard.QUERIES, contents));
    return imports;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.QUERIES_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(TemplateParameter.QUERIES_UNIT_TEST_NAME);
  }

}
