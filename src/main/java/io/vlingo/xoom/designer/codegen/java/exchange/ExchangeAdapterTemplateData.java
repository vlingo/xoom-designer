// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeAdapterTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String exchangePackage,
                                        final Stream<CodeGenerationParameter> aggregates) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    return aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .flatMap(exchange -> {
              if(exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer()) {
                return Stream.of(new ExchangeAdapterTemplateData(exchangePackage, exchange));
              } else {
                return ExchangeDetail.findConsumedQualifiedEventNames(exchange)
                        .map(qualifiedName -> new ExchangeAdapterTemplateData(codeElementFormatter, exchangePackage, qualifiedName));
              }
            }).collect(Collectors.toList());
  }

  private ExchangeAdapterTemplateData(final String exchangePackage,
                                      final CodeGenerationParameter exchange) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, exchangePackage)
                    .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, exchange.parent().value)
                    .and(TemplateParameter.SCHEMA_GROUP_NAME, exchange.retrieveRelatedValue(Label.SCHEMA_GROUP))
                    .and(TemplateParameter.EXCHANGE_ROLE, ExchangeRole.PRODUCER)
                    .andResolve(TemplateParameter.EXCHANGE_ADAPTER_NAME, JavaTemplateStandard.EXCHANGE_ADAPTER::resolveClassname)
                    .andResolve(TemplateParameter.EXCHANGE_MAPPER_NAME, JavaTemplateStandard.EXCHANGE_MAPPER::resolveClassname)
                    .addImport(IdentifiedDomainEvent.class.getCanonicalName());
  }

  private ExchangeAdapterTemplateData(final CodeElementFormatter codeElementFormatter,
                                      final String exchangePackage,
                                      final String qualifiedSchemaName) {
    this.parameters =
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, exchangePackage)
                    .and(TemplateParameter.EXCHANGE_ROLE, ExchangeRole.CONSUMER)
                    .and(TemplateParameter.LOCAL_TYPE_NAME, codeElementFormatter.simpleNameOf(qualifiedSchemaName))
                    .andResolve(TemplateParameter.EXCHANGE_ADAPTER_NAME, JavaTemplateStandard.EXCHANGE_ADAPTER::resolveClassname)
                    .andResolve(TemplateParameter.EXCHANGE_MAPPER_NAME, JavaTemplateStandard.EXCHANGE_MAPPER::resolveClassname)
                    .addImport(qualifiedSchemaName);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.EXCHANGE_ADAPTER;
  }

}
