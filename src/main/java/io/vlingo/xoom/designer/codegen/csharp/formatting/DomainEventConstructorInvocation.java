// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.DomainEventDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;

import java.util.Optional;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class DomainEventConstructorInvocation implements Formatters.Arguments {

  private final String stateVariableName;

  public DomainEventConstructorInvocation(final String stateVariableName) {
    this.stateVariableName = stateVariableName;
  }

  @Override
  public String format(final CodeGenerationParameter emitterMethod, final MethodScope scope) {
    if(!emitterMethod.hasAny(Label.DOMAIN_EVENT)) {
      return "";
    }
    final String eventName =
            emitterMethod.retrieveRelatedValue(Label.DOMAIN_EVENT);

    final CodeGenerationParameter domainEvent =
            DomainEventDetail.eventWithName(eventName, emitterMethod.parent(Label.AGGREGATE));

    return domainEvent.retrieveAllRelated(Label.STATE_FIELD)
            .map(stateField -> resolveFieldAccess(stateField, findCorrespondingMethodParameter(emitterMethod, stateField)))
            .collect(Collectors.joining(", "));
  }

  private String resolveFieldAccess(final CodeGenerationParameter eventField, final Optional<CodeGenerationParameter> correspondingMethodParameter) {
    if(!correspondingMethodParameter.isPresent()) {
      return stateVariableName + "." + toPascalCase(eventField.value);
    }

    final CodeGenerationParameter methodParameter = correspondingMethodParameter.get();
    final String fieldType = FieldDetail.typeOf(methodParameter.parent(Label.AGGREGATE), methodParameter.value);

    if(FieldDetail.isCollection(fieldType)) {
      final CollectionMutation collectionMutation =
              methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

      if(collectionMutation.isSingleParameterBased()) {
          return methodParameter.retrieveRelatedValue(Label.ALIAS);
      }
    }

    return methodParameter.value;
  }

  private Optional<CodeGenerationParameter> findCorrespondingMethodParameter(final CodeGenerationParameter emitterMethod,
                                                                             final CodeGenerationParameter stateField) {
    return emitterMethod.retrieveAllRelated(Label.METHOD_PARAMETER)
            .filter(methodParameter -> methodParameter.value.equals(stateField.value))
            .findFirst();
  }

}
