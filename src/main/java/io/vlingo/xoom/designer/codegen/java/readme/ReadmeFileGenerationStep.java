// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.readme;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.Arrays;
import java.util.List;

public class ReadmeFileGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext codeGenerationContext) {
    final String packageName = codeGenerationContext.parameterOf(Label.PACKAGE);

    final TemplateParameters templateParameters =
            TemplateParameters.with(TemplateParameter.README_FILE, true).and(TemplateParameter.PACKAGE_NAME, packageName);

    return Arrays.asList(BasicTemplateData.of(JavaTemplateStandard.README, templateParameters));
  }

}
