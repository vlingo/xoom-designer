// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.domainevent.DomainEventDetail;

import java.util.List;
import java.util.stream.Collectors;

public class EventMissingField {

  private final String fieldName;
  private final String defaultValue;

  public static List<EventMissingField> from(final CodeGenerationParameter method) {
    final CodeGenerationParameter aggregate = method.parent(Label.AGGREGATE);
    final String eventName = method.retrieveRelatedValue(Label.DOMAIN_EVENT);
    final CodeGenerationParameter domainEvent = AggregateDetail.eventWithName(aggregate, eventName);
    return method.retrieveAllRelated(Label.METHOD_PARAMETER)
            .filter(methodParameter -> !DomainEventDetail.hasField(domainEvent, methodParameter.value))
            .map(parameter -> new EventMissingField(aggregate, parameter.value))
            .collect(Collectors.toList());
  }

  private EventMissingField(final CodeGenerationParameter aggregate,
                            final String fieldName) {
    this.fieldName = fieldName;
    this.defaultValue = FieldDetail.resolveDefaultValue(aggregate, fieldName);
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

}
