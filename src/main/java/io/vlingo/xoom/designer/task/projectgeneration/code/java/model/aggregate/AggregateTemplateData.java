// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionSourceTypesDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.StorageType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.DOMAIN_EVENT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class AggregateTemplateData extends TemplateData {

  private final String protocolName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public AggregateTemplateData(final String basePackage,
                               final String packageName,
                               final CodeGenerationParameter aggregate,
                               final StorageType storageType,
                               final ProjectionType projectionType,
                               final List<Content> contents,
                               final Boolean useCQRS) {
    this.protocolName = aggregate.value;
    this.parameters = TemplateParameters.with(PACKAGE_NAME, packageName)
            .and(AGGREGATE_PROTOCOL_NAME, protocolName)
            .and(STATE_NAME, JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(protocolName))
            .and(ENTITY_NAME, JavaTemplateStandard.AGGREGATE.resolveClassname(protocolName))
            .and(ID_TYPE, FieldDetail.typeOf(aggregate, "id"))
            .and(EVENT_HANDLERS, EventHandler.from(aggregate))
            .and(SOURCED_EVENTS, resolveEventNames(aggregate))
            .addImports(resolveImports(basePackage, aggregate, projectionType, contents))
            .and(METHODS, new ArrayList<String>())
            .and(STORAGE_TYPE, storageType)
            .and(USE_CQRS, useCQRS);

    this.dependOn(AggregateMethodTemplateData.from(parameters, aggregate, storageType, projectionType));
  }

  private Set<String> resolveImports(final String basePackage,
                                     final CodeGenerationParameter aggregate,
                                     final ProjectionType projectionType,
                                     final List<Content> contents) {
    final Set<String> imports = new HashSet<>();

    imports.addAll(ValueObjectDetail.resolveImports(contents, aggregate.retrieveAllRelated(Label.STATE_FIELD)));

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
            .filter(method -> method.hasAny(DOMAIN_EVENT))
            .map(method -> method.retrieveRelatedValue(DOMAIN_EVENT))
            .collect(Collectors.toList());
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(METHODS).add(outcome);
  }

  @Override
  public String filename() {
    return standard().resolveFilename(protocolName, parameters);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.AGGREGATE;
  }

}
