// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.projections;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class ProjectionUnitTestTemplateData extends TemplateData {

  private final String projectionName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<Content> contents,
                                        final List<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects) {
    final String packageName =
        ContentQuery.findPackage(JavaTemplateStandard.PROJECTION, contents);

    final Function<CodeGenerationParameter, TemplateData> mapper =
        aggregate -> new ProjectionUnitTestTemplateData(packageName, aggregate, contents, valueObjects);

    return aggregates.stream().map(mapper).collect(Collectors.toList());
  }

  public ProjectionUnitTestTemplateData(final String packageName,
                                        final CodeGenerationParameter aggregate,
                                        final List<Content> contents,
                                        final List<CodeGenerationParameter> valueObjects) {
    this.projectionName = JavaTemplateStandard.PROJECTION.resolveClassname(aggregate.value);

    final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);
    final String aggregateState = JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value);

    this.parameters =
        TemplateParameters.with(PACKAGE_NAME, packageName)
            .and(PROJECTION_UNIT_TEST_NAME, standard().resolveClassname(projectionName.replace("Actor", "")))
            .and(PROJECTION_NAME, projectionName)
            .and(DATA_OBJECT_NAME, dataObjectName)
            .and(STATE_DATA_OBJECT_NAME, aggregateState)
            .and(TEST_CASES, TestCase.from(aggregate, valueObjects))
            .addImport(resolveImport(dataObjectName, JavaTemplateStandard.DATA_OBJECT, contents))
            .addImport(resolveImport(aggregateState, JavaTemplateStandard.AGGREGATE_STATE, contents))
            .addImports(AggregateDetail.resolveImports(aggregate))
            .and(PRODUCTION_CODE, false)
            .and(UNIT_TEST, true);
  }

  private String resolveImport(final String dataObjectName, JavaTemplateStandard dataObject, final List<Content> contents) {
    final String dataObjectPackage =
        ContentQuery.findPackage(dataObject, dataObjectName, contents);

    return CodeElementFormatter.importAllFrom(dataObjectPackage);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.PROJECTION_UNIT_TEST;
  }

  @Override
  public String filename() {
    return parameters.find(PROJECTION_UNIT_TEST_NAME);
  }

}
