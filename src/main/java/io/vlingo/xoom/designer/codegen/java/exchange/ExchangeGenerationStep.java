// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangeGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final List<CodeGenerationParameter> aggregates =
            context.parametersOf(Label.AGGREGATE).filter(aggregate -> aggregate.hasAny(Label.EXCHANGE))
                    .collect(Collectors.toList());
    final List<CodeGenerationParameter> valueObjects =
            context.parametersOf(Label.VALUE_OBJECT)
                    .collect(Collectors.toList());

    return ExchangeTemplateDataFactory.build(resolvePackage(context), aggregates, valueObjects, context.contents());
  }

  private String resolvePackage(final CodeGenerationContext context) {
    return String.format("%s.%s.%s", context.parameterOf(Label.PACKAGE), "infrastructure", "exchange");
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    if (!context.hasParameter(Label.AGGREGATE)) {
      return false;
    }
    return context.parametersOf(Label.AGGREGATE).anyMatch(aggregate -> aggregate.hasAny(Label.EXCHANGE));
  }

}
