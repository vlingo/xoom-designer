// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventHandler {

  public final String methodName;
  public final String eventName;
  public final String methodInvocationParameters;
  public final List<EventMissingField> missingFields = new ArrayList<>();

  public static List<EventHandler> from(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).map(EventHandler::new)
            .distinct().collect(Collectors.toList());
  }

  private EventHandler(final CodeGenerationParameter method) {
    this.methodName = method.value;
    this.eventName = method.retrieveRelatedValue(Label.DOMAIN_EVENT);
    this.methodInvocationParameters = Formatters.Arguments.SOURCED_STATED_METHOD_INVOCATION.format(method);
    this.missingFields.addAll(EventMissingField.from(method));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EventHandler that = (EventHandler) o;
    return eventName.equals(that.eventName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventName);
  }

}
