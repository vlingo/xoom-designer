// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.Map;

public class DatabasePropertiesTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public DatabasePropertiesTemplateData(final String appName, final Map<Model, DatabaseType> databases, final String packageName) {
    this.templateParameters = loadParameters(appName, databases, packageName);
  }

  private TemplateParameters loadParameters(final String appName, final Map<Model, DatabaseType> databases, final String packageName) {
    final TemplateParameters parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName);

    databases.forEach((key, value) -> {
      final TemplateParameter parameter = key.isQueryModel() ? TemplateParameter.QUERY_DATABASE_PARAMETER : TemplateParameter.DEFAULT_DATABASE_PARAMETER;

      parameters.and(parameter, new Database(appName, key, value));
    });

    return parameters;
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.TURBO_SETTINGS;
  }
}
