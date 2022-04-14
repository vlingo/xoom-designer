// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
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
import io.vlingo.xoom.designer.codegen.java.Aggregate;
import io.vlingo.xoom.designer.codegen.java.TurboSettings;
import io.vlingo.xoom.designer.codegen.java.resource.RouteDetail;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.codegen.template.ParameterKey.Defaults.PACKAGE_NAME;
import static java.util.stream.Collectors.toList;

public class LayoutGenerationStep extends ReactJsTemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String artifactId = context.parameterOf(Label.ARTIFACT_ID);
    final TurboSettings turboSettings = context.parameterObjectOf(Label.TURBO_SETTINGS);

    final List<Aggregate> aggregates =
            context.parametersOf(Label.AGGREGATE).filter(RouteDetail::requireModelFactory)
                    .map(Aggregate::new).collect(toList());

    return Arrays.asList(headerComponentTemplateData(artifactId), appComponentTemplateData(turboSettings, aggregates),
            sidebarComponentTemplateData(aggregates));
  }

  private TemplateData headerComponentTemplateData(final String artifactId) {
    return BasicTemplateData.of(ReactJsTemplateStandard.HEADER,
            TemplateParameters.with(PACKAGE_NAME, "src.components").and(TemplateParameter.ARTIFACT_ID, artifactId));
  }

  private TemplateData appComponentTemplateData(TurboSettings turboSettings, final List<Aggregate> aggregates) {
    return BasicTemplateData.of(ReactJsTemplateStandard.APP,
            TemplateParameters.with(PACKAGE_NAME, "src").and(TemplateParameter.AGGREGATES, aggregates)
                    .and(TemplateParameter.TURBO_SETTINGS, turboSettings));
  }

  private TemplateData sidebarComponentTemplateData(final List<Aggregate> aggregates) {
    return BasicTemplateData.of(ReactJsTemplateStandard.SIDEBAR,
            TemplateParameters.with(PACKAGE_NAME, "src.components").and(TemplateParameter.AGGREGATES, aggregates));
  }

}
