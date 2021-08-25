// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.domainevent.DomainEventDetail;

import java.util.Optional;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

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
            emitterMethod.retrieveRelatedValue(DOMAIN_EVENT);

    final CodeGenerationParameter domainEvent =
            DomainEventDetail.eventWithName(eventName, emitterMethod.parent(AGGREGATE));

    return domainEvent.retrieveAllRelated(Label.STATE_FIELD)
            .map(stateField -> resolveFieldAccess(stateField, findCorrespondingMethodParameter(emitterMethod, stateField)))
            .collect(Collectors.joining(", "));
  }

  private String resolveFieldAccess(final CodeGenerationParameter eventField, final Optional<CodeGenerationParameter> correspondingMethodParameter) {
    if(!correspondingMethodParameter.isPresent()) {
      return stateVariableName + "." + eventField.value;
    }

    final CodeGenerationParameter methodParameter = correspondingMethodParameter.get();
    final String fieldType = FieldDetail.typeOf(methodParameter.parent(AGGREGATE), methodParameter.value);

    if(FieldDetail.isCollection(fieldType)) {
      final CollectionMutation collectionMutation =
              methodParameter.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

      if(collectionMutation.isSingleParameterBased()) {
          return methodParameter.retrieveRelatedValue(ALIAS);
      }
    }

    return methodParameter.value;
  }

  private Optional<CodeGenerationParameter> findCorrespondingMethodParameter(final CodeGenerationParameter emitterMethod,
                                                                             final CodeGenerationParameter stateField) {
    return emitterMethod.retrieveAllRelated(METHOD_PARAMETER)
            .filter(methodParameter -> methodParameter.value.equals(stateField.value))
            .findFirst();
  }

}
