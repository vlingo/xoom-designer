// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ExchangeDispatcherTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static TemplateData from(final String exchangePackage,
                                  final Stream<CodeGenerationParameter> aggregates,
                                  final List<Content> contents) {
    final List<CodeGenerationParameter> producerExchanges =
            aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
                    .filter(aggregate -> aggregate.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer())
                    .collect(toList());

    return new ExchangeDispatcherTemplateData(exchangePackage, producerExchanges, contents);
  }

  private ExchangeDispatcherTemplateData(final String exchangePackage,
                                         final List<CodeGenerationParameter> producerExchanges,
                                         final List<Content> contents) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, exchangePackage)
                    .and(TemplateParameter.PRODUCER_EXCHANGES, ProducerExchange.from(producerExchanges))
                    .addImports(resolveImports(producerExchanges, contents));
  }

  private Set<String> resolveImports(final List<CodeGenerationParameter> producerExchanges,
                                     final List<Content> contents) {
    return producerExchanges.stream().flatMap(exchange -> exchange.retrieveAllRelated(Label.DOMAIN_EVENT))
            .map(event -> ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.DOMAIN_EVENT, event.value, contents))
            .collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.EXCHANGE_DISPATCHER;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean isPlaceholder() {
    return parameters.<List>find(TemplateParameter.PRODUCER_EXCHANGES).isEmpty();
  }
}
