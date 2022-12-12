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
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateProtocolTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateStateTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.model.aggregate.AggregateTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.model.domainevent.DomainEventTemplateData;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;

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
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::of);
    return context.parametersOf(Label.AGGREGATE).flatMap(aggregate -> {
      final String packageName = AggregateDetail.resolvePackage(basePackage, aggregate.value);
      return loadTemplates(packageName, aggregate, storageType, projectionType, dialect, contents, useCQRS);
    }).collect(Collectors.toList());
  }

  private static Stream<TemplateData> loadTemplates(final String packageName, final CodeGenerationParameter aggregateParameter,
                                                    final StorageType storageType, final ProjectionType projectionType,
                                                    final Dialect dialect, final List<Content> contents, final Boolean useCQRS) {
    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new AggregateProtocolTemplateData(packageName, aggregateParameter, contents, useCQRS));
    templatesData.add(new AggregateTemplateData(packageName, aggregateParameter, storageType, projectionType, useCQRS));
    templatesData.add(new AggregateStateTemplateData(packageName, dialect, aggregateParameter, storageType, contents));
    templatesData.addAll(DomainEventTemplateData.from(packageName, dialect, aggregateParameter));
    return templatesData.stream();
  }

}
