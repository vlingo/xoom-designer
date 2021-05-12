// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class MockDispatcherTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public MockDispatcherTemplateData(final String basePackage,
                                    final ProjectionType projectionType) {
    this.parameters =
            TemplateParameters.with(PROJECTION_TYPE, projectionType)
                    .and(PACKAGE_NAME, MockDispatcherDetail.resolvePackage(basePackage))
                    .and(DISPATCHER_NAME, DesignerTemplateStandard.MOCK_DISPATCHER.resolveClassname())
                    .and(PRODUCTION_CODE, false).and(UNIT_TEST, true);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.MOCK_DISPATCHER;
  }
}
