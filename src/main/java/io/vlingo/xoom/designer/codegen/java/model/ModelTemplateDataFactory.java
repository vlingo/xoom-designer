// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateProtocolTemplateData;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateStateTemplateData;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateTemplateData;
import io.vlingo.xoom.designer.codegen.java.model.domainevent.DomainEventTemplateData;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

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
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    return context.parametersOf(Label.AGGREGATE).flatMap(aggregate -> {
      final String packageName = AggregateDetail.resolvePackage(basePackage, aggregate.value);
      return loadTemplates(basePackage, packageName, dialect, aggregate, storageType, projectionType, contents, useCQRS);
    }).collect(Collectors.toList());
  }

  private static Stream<TemplateData> loadTemplates(final String basePackage,
                                                    final String packageName,
                                                    final Dialect dialect,
                                                    final CodeGenerationParameter aggregateParameter,
                                                    final StorageType storageType,
                                                    final ProjectionType projectionType,
                                                    final List<Content> contents,
                                                    final Boolean useCQRS) {
    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new AggregateProtocolTemplateData(packageName, aggregateParameter, contents, useCQRS));
    templatesData.add(new AggregateTemplateData(basePackage, packageName, aggregateParameter, storageType, projectionType, contents, useCQRS));
    templatesData.add(new AggregateStateTemplateData(packageName, dialect, aggregateParameter, storageType, contents));
    templatesData.addAll(DomainEventTemplateData.from(packageName, dialect, aggregateParameter, contents));
    return templatesData.stream();
  }

}
