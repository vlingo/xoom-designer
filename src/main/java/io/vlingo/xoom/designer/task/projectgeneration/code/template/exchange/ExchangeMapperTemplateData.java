// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.EXCHANGE_MAPPER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.EXCHANGE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.ROLE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.ExchangeRole.CONSUMER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.ExchangeRole.PRODUCER;

public class ExchangeMapperTemplateData extends TemplateData {

  private final boolean placeholder;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String exchangePackage,
                                        final Stream<CodeGenerationParameter> aggregates,
                                        final List<Content> contents) {
    final List<CodeGenerationParameter> collectedAggregates = aggregates.collect(Collectors.toList());
    final List<TemplateData> mappers = forConsumerExchanges(exchangePackage, collectedAggregates, contents);
    mappers.add(forProducerExchanges(exchangePackage, collectedAggregates));
    return mappers;
  }

  private static TemplateData forProducerExchanges(final String exchangePackage,
                                                   final List<CodeGenerationParameter> aggregates) {
    final Predicate<CodeGenerationParameter> producerPresent =
            exchange -> exchange.retrieveRelatedValue(ROLE, ExchangeRole::of).isProducer();

    final Boolean hasProducerExchange =
            aggregates.stream().flatMap(aggregate -> aggregate.retrieveAllRelated(EXCHANGE))
                    .anyMatch(producerPresent);

    return new ExchangeMapperTemplateData(exchangePackage, !hasProducerExchange);
  }

  private static List<TemplateData> forConsumerExchanges(final String exchangePackage,
                                                         final List<CodeGenerationParameter> aggregates,
                                                         final List<Content> contents) {
    final Predicate<CodeGenerationParameter> consumerPresent =
            exchange -> exchange.retrieveRelatedValue(ROLE, ExchangeRole::of).isConsumer();

    final Predicate<CodeGenerationParameter> onlyConsumerExchanges =
            aggregate -> aggregate.retrieveAllRelated(EXCHANGE).anyMatch(consumerPresent);

    final Function<CodeGenerationParameter, TemplateData> mapper =
            aggregate -> new ExchangeMapperTemplateData(exchangePackage, aggregate, contents);

    return aggregates.stream().filter(onlyConsumerExchanges).map(mapper).collect(Collectors.toList());
  }

  private ExchangeMapperTemplateData(final String exchangePackage,
                                     final CodeGenerationParameter aggregate,
                                     final List<Content> contents) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage).and(EXCHANGE_ROLE, CONSUMER)
                    .and(LOCAL_TYPE_NAME, DATA_OBJECT.resolveClassname(aggregate.value))
                    .andResolve(EXCHANGE_MAPPER_NAME, param -> standard().resolveClassname(param))
                    .addImport(resolveLocalTypeImport(aggregate, contents));

    this.placeholder = false;
  }

  private ExchangeMapperTemplateData(final String exchangePackage,
                                     final Boolean placeholder) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage).and(EXCHANGE_ROLE, PRODUCER)
                    .andResolve(EXCHANGE_MAPPER_NAME, param -> standard().resolveClassname(param));

    this.placeholder = placeholder;
  }

  private String resolveLocalTypeImport(final CodeGenerationParameter aggregate,
                                        final List<Content> contents) {
    final String dataObjectName = DATA_OBJECT.resolveClassname(aggregate.value);
    return ContentQuery.findFullyQualifiedClassName(DATA_OBJECT, dataObjectName, contents);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return EXCHANGE_MAPPER;
  }

  @Override
  public boolean isPlaceholder() {
    return placeholder;
  }

}
