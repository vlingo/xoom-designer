// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.aggregate;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class AggregateStateMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final Dialect dialect, final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .map(method -> new AggregateStateMethodTemplateData(dialect, aggregate, method))
        .collect(toList());
  }

  private AggregateStateMethodTemplateData(final Dialect dialect, final CodeGenerationParameter aggregate,
                                           final CodeGenerationParameter method) {
    this.parameters = TemplateParameters.with(TemplateParameter.METHOD_NAME, method.value)
        .and(TemplateParameter.COLLECTION_MUTATIONS, resolveCollectionMutations(dialect, method))
        .and(TemplateParameter.METHOD_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(method))
        .and(TemplateParameter.CONSTRUCTOR_PARAMETERS, resolveConstructorParameters(dialect, method))
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value));
  }

  private String resolveConstructorParameters(final Dialect dialect, final CodeGenerationParameter method) {
    return Formatters.Fields.format(Formatters.Fields.Style.SELF_ALTERNATE_REFERENCE, dialect, method.parent(), method.retrieveAllRelated(Label.METHOD_PARAMETER));
  }

  private List<String> resolveCollectionMutations(final Dialect dialect, final CodeGenerationParameter method) {
    return method.retrieveAllRelated(Label.METHOD_PARAMETER).flatMap(methodParameter -> {
      final CollectionMutation collectionMutation =
          methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);
      return collectionMutation.resolveStatements("this", dialect, methodParameter).stream();
    }).collect(Collectors.toList());
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.AGGREGATE_STATE_METHOD;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

}
