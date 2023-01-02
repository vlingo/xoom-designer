// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.PACKAGE_NAME;

public class StaticFilesGenerationStep extends ReactJsTemplateProcessingStep {

  private final String sourcePackage = "src";
  private final String publicPackage = "public";
  private final String utilsPackage = "src.utils";
  private final String componentsPackage = "src.components";

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String groupId = context.parameterOf(Label.GROUP_ID);
    final String artifactId = context.parameterOf(Label.ARTIFACT_ID);
    final String artifactVersion = context.parameterOf(Label.ARTIFACT_VERSION);

    return Arrays.asList(BasicTemplateData.of(ReactJsTemplateStandard.GIT_IGNORE),
            BasicTemplateData.of(ReactJsTemplateStandard.FORM_HANDLER, utilsPackage), BasicTemplateData.of(ReactJsTemplateStandard.FORM_MODAL, componentsPackage), BasicTemplateData.of(ReactJsTemplateStandard.INDEX, sourcePackage),
            BasicTemplateData.of(ReactJsTemplateStandard.LOADING_OR_FAILED, componentsPackage), BasicTemplateData.of(ReactJsTemplateStandard.STYLE_SHEET_INDEX, sourcePackage), BasicTemplateData.of(ReactJsTemplateStandard.HOME, componentsPackage),
            BasicTemplateData.of(ReactJsTemplateStandard.HTML_INDEX, TemplateParameters.with(PACKAGE_NAME, publicPackage).and(TemplateParameter.GROUP_ID, groupId).and(TemplateParameter.ARTIFACT_ID, artifactId)),
            BasicTemplateData.of(ReactJsTemplateStandard.PACKAGE_CONFIG, TemplateParameters.with(TemplateParameter.ARTIFACT_ID, artifactId).and(TemplateParameter.GROUP_ID, groupId).and(TemplateParameter.VERSION, artifactVersion)));
  }

}
