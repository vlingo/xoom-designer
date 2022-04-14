// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;

import java.util.List;

public class ProjectionGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    return ProjectionTemplateDataFactory.build(context);
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final String dialectName = context.parameters().retrieveValue(Label.DIALECT);
    return !dialectName.isEmpty() && Dialect.withName(dialectName).isJava() &&
        ContentQuery.exists(JavaTemplateStandard.AGGREGATE_PROTOCOL, context.contents()) && projectionType.isProjectionEnabled();
  }

}
