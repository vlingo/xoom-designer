// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

import java.util.*;
import java.util.stream.Collectors;

public class Exchange {

  public final String name;
  public final String dataObjectName;
  public final String variableName;
  public final String settingsName;
  public final Set<CoveyParameter> coveys;
  public final Set<ExchangeRole> roles = new HashSet<>();

  public static List<Exchange> from(final List<CodeGenerationParameter> aggregates) {

    final Map<String, Exchange> exchanges = new HashMap<>();

    final List<CodeGenerationParameter> allExchanges =
        aggregates.stream().flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .collect(Collectors.toList());

    aggregates.forEach(aggregate -> {
      aggregate.retrieveAllRelated(Label.EXCHANGE).forEach(exchangeParam -> {
        final ExchangeRole exchangeRole =
            exchangeParam.retrieveRelatedValue(Label.ROLE, ExchangeRole::of);

        final Exchange exchange = new Exchange(aggregate.value, exchangeParam, allExchanges);

        exchanges.computeIfAbsent(exchangeParam.value, e -> exchange).addRole(exchangeRole);
      });
    });

    return new ArrayList<>(exchanges.values());
  }

  private Exchange(final String aggregateName, final CodeGenerationParameter exchange,
                   final List<CodeGenerationParameter> allExchangeParameters) {
    this.name = exchange.value;
    this.coveys = resolveCoveyParameters(exchange.value, allExchangeParameters);
    this.variableName = Formatter.formatExchangeVariableName(exchange.value);
    this.settingsName = variableName + "Settings";
    this.dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);
  }

  private Set<CoveyParameter> resolveCoveyParameters(final String exchangeName,
                                                     final List<CodeGenerationParameter> allExchangeParameters) {
    final List<CodeGenerationParameter> relatedExchangeParameters = allExchangeParameters.stream()
        .filter(exchange -> exchange.value.equals(exchangeName))
        .collect(Collectors.toList());

    return CoveyParameter.from(relatedExchangeParameters);
  }

  private Exchange addRole(final ExchangeRole exchangeRole) {
    this.roles.add(exchangeRole);
    return this;
  }

  public boolean isProducer() {
    return this.roles.contains(ExchangeRole.PRODUCER);
  }

  public boolean isConsumer() {
    return this.roles.contains(ExchangeRole.CONSUMER);
  }
}
