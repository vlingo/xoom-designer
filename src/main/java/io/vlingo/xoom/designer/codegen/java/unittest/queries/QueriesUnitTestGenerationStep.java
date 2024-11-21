// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.queries;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.stream.Collectors;

public class QueriesUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final List<CodeGenerationParameter> aggregates =
            context.parametersOf(Label.AGGREGATE).collect(Collectors.toList());

    final List<CodeGenerationParameter> valueObjects =
            context.parametersOf(Label.VALUE_OBJECT).collect(Collectors.toList());

    return QueriesUnitTestTemplateData.from(context.contents(), aggregates, valueObjects);
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final Dialect dialect = context.parameterObjectOf(Label.DIALECT);
    return (dialect == null || dialect.isJava()) && context.parameterOf(Label.CQRS, Boolean::valueOf);
  }
}
