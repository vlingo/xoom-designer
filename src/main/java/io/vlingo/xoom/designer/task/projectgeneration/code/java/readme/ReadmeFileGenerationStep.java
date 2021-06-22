// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.readme;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.README_FILE;

public class ReadmeFileGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext codeGenerationContext) {
    final String packageName = codeGenerationContext.parameterOf(Label.PACKAGE);

    final TemplateParameters templateParameters =
            TemplateParameters.with(README_FILE, true).and(PACKAGE_NAME, packageName);

    return Arrays.asList(BasicTemplateData.of(JavaTemplateStandard.README, templateParameters));
  }

}
