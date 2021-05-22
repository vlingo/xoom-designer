// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.resource.RouteDetail;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.ReactJsTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.TemplateParameter.*;

public class StaticFilesGenerationStep extends TemplateProcessingStep {

  private final String sourcePackage = "src";
  private final String publicPackage = "public";
  private final String utilsPackage = "src.utils";
  private final String componentsPackage = "src.components";

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String groupId = context.parameterOf(Label.GROUP_ID);
    final String artifactId = context.parameterOf(Label.ARTIFACT_ID);
    final String artifactVersion = context.parameterOf(Label.VERSION);

    return Arrays.asList(BasicTemplateData.of(GIT_IGNORE),
            BasicTemplateData.of(FORM_HANDLER, utilsPackage), BasicTemplateData.of(FORM_MODAL, componentsPackage), BasicTemplateData.of(INDEX, sourcePackage),
            BasicTemplateData.of(LOADING_OR_FAILED, componentsPackage), BasicTemplateData.of(STYLE_SHEET_INDEX, sourcePackage), BasicTemplateData.of(HOME, componentsPackage),
            BasicTemplateData.of(HTML_INDEX, TemplateParameters.with(PACKAGE_NAME, publicPackage).and(GROUP_ID, groupId).and(ARTIFACT_ID, artifactId)),
            BasicTemplateData.of(PACKAGE_CONFIG, TemplateParameters.with(ARTIFACT_ID, artifactId).and(GROUP_ID, groupId).and(VERSION, artifactVersion)));
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.parametersOf(AGGREGATE).anyMatch(RouteDetail::requireEntityLoad);
  }

  @Override
  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.REACTJS;
  }
}
