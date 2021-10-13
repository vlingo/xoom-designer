// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class AggregateData {

  public final APIData api;
  public final String aggregateName;
  public final List<StateFieldData> stateFields = new ArrayList<>();
  public final List<AggregateMethodData> methods = new ArrayList<>();
  public final List<DomainEventData> events = new ArrayList<>();
  public final ConsumerExchangeData consumerExchange;
  public final ProducerExchangeData producerExchange;

  public AggregateData(final String aggregateName,
                       final APIData api,
                       final List<DomainEventData> domainEvents,
                       final List<StateFieldData> stateFields,
                       final List<AggregateMethodData> methods,
                       final ConsumerExchangeData consumerExchange,
                       final ProducerExchangeData producerExchange) {
    this.api = api;
    this.consumerExchange = consumerExchange;
    this.producerExchange = producerExchange;
    this.methods.addAll(methods);
    this.events.addAll(domainEvents);
    this.stateFields.addAll(stateFields);
    this.aggregateName = aggregateName;
  }

  public List<String> validate(List<String> errorStrings) {
    if (api == null) {
      errorStrings.add("AggregateData.api is null");
    } else {
      api.validate(errorStrings);
    }
    if (aggregateName == null) errorStrings.add("AggregateData.aggregateName is null");
    if (stateFields == null) {
      errorStrings.add("AggregateData.stateFields is null");
    } else {
      stateFields.forEach(
              stateField -> stateField.validate(errorStrings)
      );
    }
    if (methods == null) {
      errorStrings.add("AggregateData.methods is null");
    } else {
      methods.forEach(
              method -> method.validate(errorStrings)
      );
    }
    if (events == null) {
      errorStrings.add("AggregateData.events is null");
    } else {
      events.forEach(
              event -> event.validate(errorStrings)
      );
    }
    return errorStrings;
  }

  public boolean hasConsumerExchange() {
    return consumerExchange != null && !consumerExchange.receivers.isEmpty();
  }

  public boolean hasProducerExchange() {
    return producerExchange != null && !producerExchange.outgoingEvents.isEmpty();
  }
}