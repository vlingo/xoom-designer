// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateProtocolTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateStateTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateTemplateData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelTemplateDataFactory {

  public static List<TemplateData> from(final CodeGenerationContext context) {
    final List<Content> contents = context.contents();
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    final Dialect dialect = context.parameterOf(Label.DIALECT, Dialect::valueOf);
    return context.parametersOf(Label.AGGREGATE).flatMap(aggregate -> {
      final String packageName = AggregateDetail.resolvePackage(basePackage, aggregate.value);
      return loadTemplates(packageName, aggregate,dialect, contents, useCQRS);
    }).collect(Collectors.toList());
  }

  private static Stream<TemplateData> loadTemplates(final String packageName,
                                                    final CodeGenerationParameter aggregateParameter,
                                                    final Dialect dialect,
                                                    final List<Content> contents, final Boolean useCQRS) {
    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new AggregateProtocolTemplateData(packageName, aggregateParameter, contents, useCQRS));
    templatesData.add(new AggregateTemplateData(packageName, aggregateParameter, useCQRS));
    templatesData.add(new AggregateStateTemplateData(packageName, dialect, aggregateParameter, contents));
    return templatesData.stream();
  }

}
