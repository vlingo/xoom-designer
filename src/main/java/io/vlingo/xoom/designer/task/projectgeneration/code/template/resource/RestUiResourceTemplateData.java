// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class RestUiResourceTemplateData extends TemplateData {

  private final String packageName;
  private final TemplateParameters parameters;

  public RestUiResourceTemplateData(final String basePackage, final Language language) {
    this.packageName = resolvePackage(basePackage);
    this.parameters = loadParameters();
  }

  private TemplateParameters loadParameters() {
    return TemplateParameters.with(PACKAGE_NAME, packageName);
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "infrastructure", "resource");
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.REST_UI_RESOURCE;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public String filename() {
    return "UiResource";
  }

}
