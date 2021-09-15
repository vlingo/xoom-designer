// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionSourceTypesDetail;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final TemplateParameters parentParameters,
                                        final CodeGenerationParameter aggregate,
                                        final StorageType storageType,
                                        final ProjectionType projectionType) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .map(method -> new AggregateMethodTemplateData(parentParameters, method, storageType, projectionType))
            .collect(toList());
  }

  private AggregateMethodTemplateData(final TemplateParameters parentParameters,
                                      final CodeGenerationParameter method,
                                      final StorageType storageType,
                                      final ProjectionType projectionType) {

    this.parameters =
            TemplateParameters.with(TemplateParameter.METHOD_NAME, method.value).and(TemplateParameter.STORAGE_TYPE, storageType)
                    .and(TemplateParameter.DOMAIN_EVENT_NAME, method.retrieveRelatedValue(Label.DOMAIN_EVENT))
                    .and(TemplateParameter.METHOD_INVOCATION_PARAMETERS, Formatters.Arguments.AGGREGATE_METHOD_INVOCATION.format(method))
                    .and(TemplateParameter.DOMAIN_EVENT_CONSTRUCTOR_PARAMETERS, Formatters.Arguments.DOMAIN_EVENT_CONSTRUCTOR_INVOCATION.format(method))
                    .and(TemplateParameter.METHOD_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(method))
                    .and(TemplateParameter.SOURCED_EVENTS, SourcedEvent.from(method.parent()))
                    .and(TemplateParameter.OPERATION_BASED, projectionType.isOperationBased())
                    .and(TemplateParameter.PROJECTION_SOURCE_TYPES_NAME, resolveProjectionSourceTypesName(projectionType))
                    .and(TemplateParameter.STATE_NAME, JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(method.parent(Label.AGGREGATE).value));

    parentParameters.addImports(resolveImports(method));
  }

  private Set<String> resolveImports(final CodeGenerationParameter method) {
    final Stream<CodeGenerationParameter> involvedStateFields =
            AggregateDetail.findInvolvedStateFields(method.parent(), method.value);

    return AggregateDetail.resolveImports(involvedStateFields);
  }

  private String resolveProjectionSourceTypesName(final ProjectionType projectionType) {
    return ProjectionSourceTypesDetail.resolveClassName(projectionType);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.AGGREGATE_METHOD;
  }

}
