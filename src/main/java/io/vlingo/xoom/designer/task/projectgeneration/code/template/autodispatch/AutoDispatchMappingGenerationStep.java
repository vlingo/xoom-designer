// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.autodispatch;

import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateProcessingStep;

import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.USE_AUTO_DISPATCH;

public class AutoDispatchMappingGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    return AutoDispatchMappingTemplateDataFactory.build(context.parameters(), context.contents());
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.hasParameter(USE_AUTO_DISPATCH) &&
            context.parameterOf(USE_AUTO_DISPATCH, Boolean::valueOf);
  }

}