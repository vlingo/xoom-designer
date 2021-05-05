// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.EXCHANGE_BOOTSTRAP;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.EXCHANGE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.ROLE;
import static io.vlingo.xoom.turbo.codegen.template.TemplateParameter.*;

public class ExchangeBootstrapTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static TemplateData from(final String exchangePackage,
                                  final Stream<CodeGenerationParameter> aggregates,
                                  final List<Content> contents) {
    final List<CodeGenerationParameter> exchanges =
            aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(EXCHANGE))
                    .collect(Collectors.toList());

    return new ExchangeBootstrapTemplateData(exchangePackage, exchanges, contents);
  }

  public ExchangeBootstrapTemplateData(final String exchangePackage,
                                       final List<CodeGenerationParameter> exchanges,
                                       final List<Content> contents) {
    parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage).and(EXCHANGES, Exchange.from(exchanges))
                    .and(EXCHANGE_BOOTSTRAP_NAME, EXCHANGE_BOOTSTRAP.resolveClassname())
                    .and(PRODUCER_EXCHANGES, joinProducerExchangeNames(exchanges))
                    .addImports(resolveImports(exchanges, contents));
  }

  private String joinProducerExchangeNames(final List<CodeGenerationParameter> exchanges) {
    final Predicate<CodeGenerationParameter> onlyProducers =
            exchange -> exchange.retrieveRelatedValue(ROLE, ExchangeRole::of).isProducer();

    return exchanges.stream().filter(onlyProducers).map(Formatter::formatExchangeVariableName)
            .distinct().collect(Collectors.joining(", "));
  }

  private Set<String> resolveImports(final List<CodeGenerationParameter> exchanges,
                                     final List<Content> contents) {
    final Set<String> imports = exchanges.stream()
            .filter(exchange -> exchange.retrieveRelatedValue(ROLE, ExchangeRole::of).isConsumer())
            .map(exchange -> DATA_OBJECT.resolveClassname(exchange.parent(Label.AGGREGATE).value))
            .map(dataObjectName -> ContentQuery.findFullyQualifiedClassName(DATA_OBJECT, dataObjectName, contents))
            .collect(Collectors.toSet());

    final Predicate<CodeGenerationParameter> hasProducerExchanges =
            exchange -> exchange.retrieveRelatedValue(ROLE, ExchangeRole::of).isProducer();

    if (exchanges.stream().anyMatch(hasProducerExchanges)) {
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
    return EXCHANGE_BOOTSTRAP;
  }
}
