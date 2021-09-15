// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.SchemataSettings;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

public class SchemataDNSTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public SchemataDNSTemplateData(final SchemataSettings schemataSettings) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.POM_SECTION, true)
                    .and(TemplateParameter.PRODUCTION_CODE, false)
                    .and(TemplateParameter.OFFSET, "<profiles>")
                    .and(TemplateParameter.SERVICE_NAME, schemataSettings.serviceDNS.get()._1)
                    .and(TemplateParameter.SERVICE_PORT, schemataSettings.serviceDNS.get()._2);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.SCHEMATA_DNS;
  }
}
