// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class ProjectionTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationContext context) {
    final List<Content> contents = context.contents();

    final String basePackage = context.parameterOf(PACKAGE);

    final Boolean useAnnotations =
            context.parameterOf(USE_ANNOTATIONS, Boolean::valueOf);

    final ProjectionType projectionType =
            context.parameterOf(PROJECTION_TYPE, ProjectionType::valueOf);

    final List<CodeGenerationParameter> valueObjects =
            context.parametersOf(VALUE_OBJECT).collect(Collectors.toList());

    final Stream<CodeGenerationParameter> aggregates = context.parametersOf(AGGREGATE);

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(ProjectionSourceTypesTemplateData.from(basePackage, projectionType, contents));
    templatesData.add(ProjectionDispatcherProviderTemplateData.from(basePackage, projectionType, useAnnotations, contents));
    templatesData.addAll(ProjectionTemplateData.from(basePackage, aggregates, valueObjects, projectionType, contents));
    return templatesData;
  }

}
