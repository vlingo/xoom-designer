// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.model;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectTemplateData;

import java.util.List;
import java.util.stream.Stream;

public class ValueObjectGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final Dialect dialect = context.parameterOf(Label.DIALECT, Dialect::valueOf);
    final Stream<CodeGenerationParameter> valueObjects = context.parametersOf(Label.VALUE_OBJECT);
    return ValueObjectTemplateData.from(basePackage, dialect, valueObjects);
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.hasParameter(Label.VALUE_OBJECT);
  }

}
