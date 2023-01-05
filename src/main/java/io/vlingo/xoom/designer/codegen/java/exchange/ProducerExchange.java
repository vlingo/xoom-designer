// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProducerExchange {

  private final String name;
  private final Set<String> events;

  public static List<ProducerExchange> from(final List<CodeGenerationParameter> exchanges) {
    return exchanges.stream().map(exchange -> exchange.value).distinct()
            .map(name -> new ProducerExchange(name, exchanges))
            .collect(Collectors.toList());
  }

  private ProducerExchange(final String exchangeName,
                           final List<CodeGenerationParameter> allExchangeParameters) {
    this.name = exchangeName;
    this.events = allExchangeParameters.stream().filter(exchange -> exchange.value.equals(exchangeName))
            .flatMap(exchange -> exchange.retrieveAllRelated(Label.DOMAIN_EVENT))
            .map(event -> event.value).collect(Collectors.toSet());
  }

  public String getName() {
    return name;
  }

  public Set<String> getEvents() {
    return events;
  }
}
