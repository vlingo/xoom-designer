// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.designermodel;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

public class DesignerModelTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public DesignerModelTemplateData(final String appName,
                                   final String designerModel) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.DESIGNER_MODEL, true)
                    .and(TemplateParameter.DESIGNER_MODEL_JSON, DesignerModelFormatter.format(designerModel))
                    .and(TemplateParameter.APPLICATION_NAME, appName);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.DESIGNER_MODEL;
  }
}
