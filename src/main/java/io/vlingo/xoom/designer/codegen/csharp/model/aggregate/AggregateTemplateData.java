// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.*;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AggregateTemplateData extends TemplateData {

  private final String protocolName;
  private final String aggregateName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public AggregateTemplateData(final String packageName, final CodeGenerationParameter aggregate,
                               final StorageType storageType, final ProjectionType projectionType,
                               final Boolean useCQRS) {
    this.protocolName = AggregateDetail.resolveProtocolNameFor(aggregate);
    this.aggregateName = aggregate.value;
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, protocolName)
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregateName))
        .and(TemplateParameter.ENTITY_NAME, CsharpTemplateStandard.AGGREGATE.resolveClassname(aggregateName))
        .and(TemplateParameter.ID_TYPE, FieldDetail.typeOf(aggregate, AggregateDetail.findIdField(aggregate).value))
        .and(TemplateParameter.EVENT_HANDLERS, EventHandler.from(aggregate))
        .and(TemplateParameter.SOURCED_EVENTS, resolveEventNames(aggregate))
        .addImports(resolveImports(aggregate, packageName, projectionType))
        .and(TemplateParameter.METHODS, new ArrayList<String>())
        .and(TemplateParameter.STORAGE_TYPE, storageType)
        .and(TemplateParameter.USE_CQRS, useCQRS);

    this.dependOn(AggregateMethodTemplateData.from(parameters, aggregate, storageType));
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate, final String basePackage,
                                      final ProjectionType projectionType) {
    final Set<String> imports = new HashSet<>();

    if (aggregate.hasAny(Label.AGGREGATE_METHOD)) {
      imports.add(Completes.class.getCanonicalName());
    }

    if (projectionType.isOperationBased()) {
      imports.add(ProjectionSourceTypesDetail.resolveQualifiedName(basePackage, projectionType));
    }
    return imports;
  }

  private List<String> resolveEventNames(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .filter(method -> method.hasAny(Label.DOMAIN_EVENT))
        .map(method -> method.retrieveRelatedValue(Label.DOMAIN_EVENT))
        .collect(Collectors.toList());
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(TemplateParameter.METHODS).add(outcome);
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateName, parameters);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.AGGREGATE;
  }

}
