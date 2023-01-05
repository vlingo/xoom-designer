// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BootstrapTemplateData extends TemplateData {

  private final static String PACKAGE_PATTERN = "%s.%s";
  private final static String INFRA_PACKAGE_NAME = "Infrastructure";

  private final TemplateParameters parameters;

  protected BootstrapTemplateData() {
    this.parameters = TemplateParameters.empty();
  }

  public static TemplateData from(final CodeGenerationContext context) {
    final Predicate<BootstrapTemplateData> supportCondition = templateData -> templateData.support(context);

    final BootstrapTemplateData bootstrapTemplateData = Stream.of(new DefaultBootstrapTemplateData())
        .filter(supportCondition).findFirst().get();

    bootstrapTemplateData.handleParameters(context);

    return bootstrapTemplateData;
  }

  private void handleParameters(final CodeGenerationContext context) {
    loadParameters(context);
    enrichParameters(context);
  }

  private TemplateParameters loadParameters(final CodeGenerationContext context) {
    final String packageName = resolvePackage(context.parameterOf(Label.PACKAGE));

    return this.parameters
        .and(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.APPLICATION_NAME, context.parameterOf(Label.APPLICATION_NAME));
  }

  protected abstract void enrichParameters(final CodeGenerationContext context);

  protected abstract boolean support(final CodeGenerationContext context);

  protected String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, INFRA_PACKAGE_NAME);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.BOOTSTRAP;
  }

}
