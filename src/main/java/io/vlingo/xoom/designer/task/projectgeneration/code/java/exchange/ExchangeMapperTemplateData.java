// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole.CONSUMER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole.PRODUCER;

public class ExchangeMapperTemplateData extends TemplateData {

  private final boolean placeholder;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String exchangePackage,
                                        final Stream<CodeGenerationParameter> aggregates) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    final List<CodeGenerationParameter> collectedAggregates = aggregates.collect(Collectors.toList());
    final List<TemplateData> mappers = forConsumerExchanges(codeElementFormatter, exchangePackage, collectedAggregates);
    mappers.add(forProducerExchanges(exchangePackage, collectedAggregates));
    return mappers;
  }

  private static TemplateData forProducerExchanges(final String exchangePackage,
                                                   final List<CodeGenerationParameter> aggregates) {
    final Predicate<CodeGenerationParameter> producerPresent =
            exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer();

    final Boolean hasProducerExchange =
            aggregates.stream().flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
                    .anyMatch(producerPresent);

    return new ExchangeMapperTemplateData(exchangePackage, !hasProducerExchange);
  }

  private static List<TemplateData> forConsumerExchanges(final CodeElementFormatter codeElementFormatter,
                                                         final String exchangePackage,
                                                         final List<CodeGenerationParameter> aggregates) {
    return aggregates.stream().flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .flatMap(exchange -> ExchangeDetail.findConsumedQualifiedEventNames(exchange))
            .map(schemaQualifiedName -> new ExchangeMapperTemplateData(codeElementFormatter, exchangePackage, schemaQualifiedName))
            .collect(Collectors.toList());
  }

  private ExchangeMapperTemplateData(final CodeElementFormatter codeElementFormatter,
                                     final String exchangePackage,
                                     final String schemaQualifiedName) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage).and(EXCHANGE_ROLE, CONSUMER)
                    .and(LOCAL_TYPE_NAME, codeElementFormatter.simpleNameOf(schemaQualifiedName))
                    .andResolve(EXCHANGE_MAPPER_NAME, param -> standard().resolveClassname(param))
                    .addImport(schemaQualifiedName);

    this.placeholder = false;
  }

  private ExchangeMapperTemplateData(final String exchangePackage,
                                     final Boolean placeholder) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage).and(EXCHANGE_ROLE, PRODUCER)
                    .andResolve(EXCHANGE_MAPPER_NAME, param -> standard().resolveClassname(param));

    this.placeholder = placeholder;
  }


  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.EXCHANGE_MAPPER;
  }

  @Override
  public boolean isPlaceholder() {
    return placeholder;
  }

}
