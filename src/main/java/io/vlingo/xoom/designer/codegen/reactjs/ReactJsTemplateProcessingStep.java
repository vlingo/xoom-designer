// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.resource.RouteDetail;

import java.util.stream.Stream;

public abstract class ReactJsTemplateProcessingStep extends TemplateProcessingStep {

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final Dialect webUIDialect = context.parameterOf(Label.WEB_UI_DIALECT, Dialect::withName);
    final Stream<CodeGenerationParameter> aggregates = context.parametersOf(Label.AGGREGATE);
    return webUIDialect != null && webUIDialect.equals(Dialect.REACTJS) && aggregates.anyMatch(RouteDetail::requireModelFactory);
  }

  @Override
  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.REACTJS;
  }

}
