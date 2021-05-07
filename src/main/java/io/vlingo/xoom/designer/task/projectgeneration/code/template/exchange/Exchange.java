// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Exchange {

  private final String name;
  private final String exchangeVariableName;
  private final Set<CoveyParameter> coveys;

  public static List<Exchange> from(final List<CodeGenerationParameter> exchanges) {
    return exchanges.stream().map(exchange -> exchange.value).distinct()
            .map(exchangeName -> new Exchange(exchangeName, exchanges))
            .collect(Collectors.toList());
  }

  public Exchange(final String exchangeName,
                  final List<CodeGenerationParameter> allExchangeParameters) {
    this.name = exchangeName;
    this.exchangeVariableName = Formatter.formatExchangeVariableName(exchangeName);
    this.coveys = resolveCoveyParameters(exchangeName, allExchangeParameters);
  }

  private Set<CoveyParameter> resolveCoveyParameters(final String exchangeName,
                                                     final List<CodeGenerationParameter> allExchangeParameters) {
    final List<CodeGenerationParameter> relatedExchangeParameters =
            allExchangeParameters.stream().filter(exchange -> exchange.value.equals(exchangeName))
                    .collect(Collectors.toList());

    return CoveyParameter.from(relatedExchangeParameters);
  }

  public String getVariableName() {
    return exchangeVariableName;
  }

  public String getSettingsName() {
    return exchangeVariableName + "Settings";
  }

  public Set<CoveyParameter> getCoveys() {
    return coveys;
  }

  public String getName() {
    return name;
  }

}
