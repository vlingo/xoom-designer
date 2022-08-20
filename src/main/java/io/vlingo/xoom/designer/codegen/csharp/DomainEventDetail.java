// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DomainEventDetail {

  public static CodeGenerationParameter eventWithName(final String eventName, final CodeGenerationParameter aggregate) {
    return eventWithName(eventName, aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).collect(Collectors.toList()));
  }

  public static CodeGenerationParameter eventWithName(final String eventName, final List<CodeGenerationParameter> events) {
    return events.stream().filter(event -> event.value.equals(eventName)).findFirst().get();
  }

  public static boolean hasField(final CodeGenerationParameter domainEvent, final String fieldName) {
    return fieldWithName(domainEvent, fieldName).isPresent();
  }

  public static Optional<CodeGenerationParameter> fieldWithName(final CodeGenerationParameter domainEvent, final String fieldName) {
    return domainEvent.retrieveAllRelated(Label.STATE_FIELD).filter(field -> field.value.equals(fieldName)).findFirst();
  }
}
