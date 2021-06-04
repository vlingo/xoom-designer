// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model.domainevent;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import java.util.List;

public class DomainEventDetail {

  public static boolean hasField(final String eventName, final String fieldName, final List<CodeGenerationParameter> events) {
    return events.stream().filter(event -> event.value.equals(eventName)).anyMatch(event -> hasField(event, fieldName));
  }

  public static boolean hasField(final CodeGenerationParameter domainEvent, final String fieldName) {
    return domainEvent.retrieveAllRelated(Label.STATE_FIELD).anyMatch(field -> field.value.equals(fieldName));
  }

  public static boolean isEmittedByFactoryMethod(final String eventName, final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .filter(method -> method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf))
            .anyMatch(method -> method.retrieveRelatedValue(Label.DOMAIN_EVENT).equals(eventName));
  }

}
