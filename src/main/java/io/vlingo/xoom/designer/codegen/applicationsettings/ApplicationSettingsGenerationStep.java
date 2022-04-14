// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.applicationsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;

public class ApplicationSettingsGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    return applicationSettingsDataFactory(context)
        .generate();
  }

  private ApplicationSettingsData applicationSettingsDataFactory(CodeGenerationContext context) {
    final String dialectName = dialectNameFrom(context);

    ApplicationSettingsData applicationSettingsData;
    if(dialectName.isEmpty() || Dialect.withName(dialectName).isJava()) {
      applicationSettingsData = new JavaApplicationSettingsData(context);
    } else {
      applicationSettingsData = new CsharpApplicationSettingsData(context);
    }
    return applicationSettingsData;
  }

  @Override
  protected Dialect resolveDialect(CodeGenerationContext context) {
    final String dialectName = dialectNameFrom(context);
    return dialectName.isEmpty()? super.resolveDialect(context) : Dialect.withName(dialectName);
  }

  private String dialectNameFrom(CodeGenerationContext context) {
    return context.parameters().retrieveValue(Label.DIALECT);
  }
}
