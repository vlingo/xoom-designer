// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.schemata.Schema;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoveyParameter {

  public final String localClass;
  public final String externalClass;
  public final String adapterInstantiation;
  public final String receiverInstantiation;
  public final String qualifiedLocalClassName;

  public static Set<CoveyParameter> from(final List<CodeGenerationParameter> exchanges) {
    final Predicate<CodeGenerationParameter> onlyConsumers =
            exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isConsumer();

    final Predicate<CodeGenerationParameter> onlyProducers =
            exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer();

    final Set<CoveyParameter> consumerCoveys =
            exchanges.stream().filter(onlyConsumers)
                    .flatMap(exchange -> exchange.retrieveAllRelated(Label.RECEIVER))
                    .map(receiver -> new CoveyParameter(receiver.parent(Label.EXCHANGE), receiver.retrieveOneRelated(Label.SCHEMA).object()))
                    .collect(Collectors.toSet());

    final Set<CoveyParameter> producerCoveys =
            exchanges.stream().filter(onlyProducers).map(CoveyParameter::new).collect(Collectors.toSet());

    return Stream.of(consumerCoveys, producerCoveys).flatMap(Set::stream).collect(Collectors.toSet());
  }

  private CoveyParameter(final CodeGenerationParameter consumerExchange,
                         final Schema schema) {
    this.localClass = schema.simpleClassName();
    this.externalClass = String.class.getSimpleName();
    this.adapterInstantiation = resolveConsumerAdapterInstantiation(schema);
    this.receiverInstantiation = String.format("new %s(stage)", resolveReceiverName(consumerExchange, schema));
    this.qualifiedLocalClassName = schema.qualifiedName();
  }

  private CoveyParameter(final CodeGenerationParameter producerExchange) {
    this.localClass = IdentifiedDomainEvent.class.getSimpleName();
    this.externalClass = IdentifiedDomainEvent.class.getSimpleName();
    this.adapterInstantiation = String.format("new %s()", resolveProducerAdapterName(producerExchange));
    this.receiverInstantiation = "received -> {}";
    this.qualifiedLocalClassName = IdentifiedDomainEvent.class.getCanonicalName();
  }

  private String resolveConsumerAdapterInstantiation(final Schema schema) {
    return String.format("new %s(\"%s\")", resolveConsumerAdapterName(schema), schema.reference);
  }

  private String resolveReceiverName(final CodeGenerationParameter consumerExchange,
                                     final Schema schema) {
    final TemplateParameters aggregateProtocolName =
            TemplateParameters.with(TemplateParameter.AGGREGATE_PROTOCOL_NAME, consumerExchange.parent().value);

    final String holderName =
            JavaTemplateStandard.EXCHANGE_RECEIVER_HOLDER.resolveClassname(aggregateProtocolName);

    return String.format("%s.%s", holderName, schema.innerReceiverClassName());
  }

  private String resolveProducerAdapterName(final CodeGenerationParameter exchange) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.AGGREGATE_PROTOCOL_NAME, exchange.parent().value)
                    .and(TemplateParameter.EXCHANGE_ROLE, ExchangeRole.PRODUCER);
    return JavaTemplateStandard.EXCHANGE_ADAPTER.resolveClassname(parameters);
  }

  private String resolveConsumerAdapterName(final Schema schema) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.LOCAL_TYPE_NAME, schema.simpleClassName())
                    .and(TemplateParameter.EXCHANGE_ROLE, ExchangeRole.CONSUMER);

    return JavaTemplateStandard.EXCHANGE_ADAPTER.resolveClassname(parameters);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CoveyParameter that = (CoveyParameter) o;
    return Objects.equals(localClass, that.localClass) &&
            Objects.equals(adapterInstantiation, that.adapterInstantiation) &&
            Objects.equals(externalClass, that.externalClass) &&
            Objects.equals(receiverInstantiation, that.receiverInstantiation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(localClass, adapterInstantiation, externalClass, receiverInstantiation);
  }
}
