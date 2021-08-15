// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;

import java.util.Collections;
import java.util.List;

public class RestResourceAbstractUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    return Collections.singletonList(RestResourceUnitTestTemplateDataFactory.buildAbstract(context.parameters(), context.contents()));
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    //TODO: Remove this method when https://trello.com/c/ikCGH4fb is done.
    return false;
  }
}