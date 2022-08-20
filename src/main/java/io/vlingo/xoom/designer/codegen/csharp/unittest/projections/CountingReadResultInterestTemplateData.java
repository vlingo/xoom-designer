package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

public class CountingReadResultInterestTemplateData extends TemplateData {
  private final TemplateParameters parameters;

  public CountingReadResultInterestTemplateData(String basePackage) {
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, basePackage)
        .and(TemplateParameter.PRODUCTION_CODE, false)
        .and(TemplateParameter.UNIT_TEST, true);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.COUNTING_READ_RESULT;
  }
}
