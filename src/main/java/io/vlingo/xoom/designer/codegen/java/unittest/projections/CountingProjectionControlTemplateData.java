package io.vlingo.xoom.designer.codegen.java.unittest.projections;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

public class CountingProjectionControlTemplateData extends TemplateData {
  private final TemplateParameters parameters;

  public CountingProjectionControlTemplateData(String basePackage) {
    this.parameters =
        TemplateParameters.with(TemplateParameter.PACKAGE_NAME, basePackage)
            .and(TemplateParameter.PRODUCTION_CODE, false).and(TemplateParameter.UNIT_TEST, true);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.COUNTING_PROJECTION_CTL;
  }
}
