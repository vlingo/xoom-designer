// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.projectgenerationsettings;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

public class ProjectSettingsTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public ProjectSettingsTemplateData(final String appName,
                                     final String generationSettings) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.PROJECT_SETTINGS, true)
                    .and(TemplateParameter.PROJECT_SETTINGS_PAYLOAD, formatJson(generationSettings))
                    .and(TemplateParameter.APPLICATION_NAME, appName);
  }

  private String formatJson(final String generationSettingsPayload) {
    final JsonElement parsed = new JsonParser().parse(generationSettingsPayload);
    return new GsonBuilder().setPrettyPrinting().create().toJson(parsed);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.PROJECT_SETTINGS;
  }
}
