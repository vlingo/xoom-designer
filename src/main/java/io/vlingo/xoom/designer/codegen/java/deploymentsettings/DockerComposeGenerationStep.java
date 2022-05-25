// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.Collections;
import java.util.List;

public class DockerComposeGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final TemplateParameters templateParameters =
        TemplateParameters.with(TemplateParameter.DOCKER_COMPOSE_FILE, true);

    return Collections.singletonList(BasicTemplateData.of(JavaTemplateStandard.DOCKER_COMPOSE, templateParameters));
  }

  @Override
  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.DOCKER;
  }
}
