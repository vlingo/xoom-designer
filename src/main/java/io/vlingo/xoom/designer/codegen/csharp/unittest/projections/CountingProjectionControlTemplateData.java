package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

public class CountingProjectionControlTemplateData extends TemplateData {
  private final TemplateParameters parameters;

  public CountingProjectionControlTemplateData(String basePackage) {
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, resolvePackage(basePackage))
            .and(TemplateParameter.PRODUCTION_CODE, false).and(TemplateParameter.UNIT_TEST, true);
  }

  private static String resolvePackage(final String basePackage) {
    return basePackage + ".Tests.Infrastructure.Persistence";
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.COUNTING_PROJECTION_CTL;
  }
}
