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
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.EXCHANGE_ADAPTER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.EXCHANGE_MAPPER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

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
            TemplateParameters.with(PACKAGE_NAME, exchangePackage)
                    .and(AGGREGATE_PROTOCOL_NAME, exchange.parent().value)
                    .and(SCHEMA_GROUP_NAME, exchange.retrieveRelatedValue(Label.SCHEMA_GROUP))
                    .and(EXCHANGE_ROLE, ExchangeRole.PRODUCER)
                    .andResolve(EXCHANGE_ADAPTER_NAME, EXCHANGE_ADAPTER::resolveClassname)
                    .andResolve(EXCHANGE_MAPPER_NAME, EXCHANGE_MAPPER::resolveClassname)
                    .addImport(IdentifiedDomainEvent.class.getCanonicalName());
  }

  private ExchangeAdapterTemplateData(final CodeElementFormatter codeElementFormatter,
                                      final String exchangePackage,
                                      final String qualifiedSchemaName) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage)
                    .and(EXCHANGE_ROLE, ExchangeRole.CONSUMER)
                    .and(LOCAL_TYPE_NAME, codeElementFormatter.simpleNameOf(qualifiedSchemaName))
                    .andResolve(EXCHANGE_ADAPTER_NAME, EXCHANGE_ADAPTER::resolveClassname)
                    .andResolve(EXCHANGE_MAPPER_NAME, EXCHANGE_MAPPER::resolveClassname)
                    .addImport(qualifiedSchemaName);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return EXCHANGE_ADAPTER;
  }

}
