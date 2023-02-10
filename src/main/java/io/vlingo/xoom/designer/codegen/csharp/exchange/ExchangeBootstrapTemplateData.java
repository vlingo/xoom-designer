// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExchangeBootstrapTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static TemplateData from(final String exchangePackage,
                                  final List<Exchange> exchanges) {
    return new ExchangeBootstrapTemplateData(exchangePackage, exchanges);
  }

  public ExchangeBootstrapTemplateData(final String exchangePackage,
                                       final List<Exchange> exchanges) {
    parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, exchangePackage)
        .and(TemplateParameter.EXCHANGES, exchanges)
        .and(TemplateParameter.EXCHANGE_BOOTSTRAP_NAME, CsharpTemplateStandard.EXCHANGE_BOOTSTRAP.resolveClassname())
        .and(TemplateParameter.PRODUCER_EXCHANGES, joinProducerExchangeNames(exchanges))
        .addImports(resolveImports(exchanges));
  }

  private String joinProducerExchangeNames(final List<Exchange> exchanges) {
    return exchanges.stream().filter(Exchange::isProducer).map(exchange -> Formatter.formatExchangeVariableName(exchange.name))
        .distinct().collect(Collectors.joining(", "));
  }

  private Set<String> resolveImports(final List<Exchange> exchanges) {
    final Set<String> imports = exchanges.stream().filter(Exchange::isConsumer)
        .flatMap(exchange -> exchange.coveys.stream())
        .map(covey -> covey.qualifiedLocalClassName)
        .collect(Collectors.toSet());

    if (exchanges.stream().anyMatch(Exchange::isProducer)) {
      imports.add(IdentifiedDomainEvent.class.getCanonicalName());
    }

    return imports;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.EXCHANGE_BOOTSTRAP;
  }
}
