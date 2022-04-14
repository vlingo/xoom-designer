// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.entity;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;

public class MockDispatcherTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public MockDispatcherTemplateData(final String basePackage,
                                    final ProjectionType projectionType) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.PROJECTION_TYPE, projectionType)
                    .and(TemplateParameter.PACKAGE_NAME, MockDispatcherDetail.resolvePackage(basePackage))
                    .and(TemplateParameter.DISPATCHER_NAME, JavaTemplateStandard.MOCK_DISPATCHER.resolveClassname())
                    .and(TemplateParameter.PRODUCTION_CODE, false).and(TemplateParameter.UNIT_TEST, true);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.MOCK_DISPATCHER;
  }
}
