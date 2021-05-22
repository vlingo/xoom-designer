// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class ExchangeReceiverHolderTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final Dialect dialect,
                                        final String exchangePackage,
                                        final Stream<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final List<Content> contents) {
    return aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .filter(exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isConsumer())
            .map(exchange -> new ExchangeReceiverHolderTemplateData(dialect, exchangePackage, exchange, valueObjects, contents))
            .collect(Collectors.toList());
  }

  private ExchangeReceiverHolderTemplateData(final Dialect dialect,
                                             final String exchangePackage,
                                             final CodeGenerationParameter exchange,
                                             final List<CodeGenerationParameter> valueObjects,
                                             final List<Content> contents) {
    final List<ExchangeReceiver> receiversParameters =
            ExchangeReceiver.from(dialect, exchange, valueObjects);

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, exchangePackage)
                    .and(AGGREGATE_PROTOCOL_NAME, exchange.parent().value)
                    .andResolve(EXCHANGE_RECEIVER_HOLDER_NAME, params -> standard().resolveClassname(params))
                    .addImports(resolveImports(exchange, receiversParameters, contents))
                    .and(EXCHANGE_RECEIVERS, receiversParameters);
  }

  private Set<String> resolveImports(final CodeGenerationParameter exchange,
                                     final List<ExchangeReceiver> receiversParameters,
                                     final List<Content> contents) {
    final CodeGenerationParameter aggregate = exchange.parent();

    final List<TemplateStandard> standards =
            Stream.of(JavaTemplateStandard.DATA_OBJECT, JavaTemplateStandard.AGGREGATE_PROTOCOL).collect(Collectors.toList());

    if (receiversParameters.stream().anyMatch(receiver -> !receiver.isModelFactoryMethod())) {
      standards.add(JavaTemplateStandard.AGGREGATE);
    }

    final Set<String> imports = standards.stream().map(standard -> {
      final String typeName = standard.resolveClassname(aggregate.value);
      return ContentQuery.findFullyQualifiedClassName(standard, typeName, contents);
    }).collect(Collectors.toSet());

    final Stream<CodeGenerationParameter> involvedStateFields =
            ExchangeDetail.findInvolvedStateFieldsOnReceivers(exchange);

    final Set<String> valueObjects =
            ValueObjectDetail.resolveImports(contents, involvedStateFields);

    return Stream.of(imports, valueObjects).flatMap(Set::stream).collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.EXCHANGE_RECEIVER_HOLDER;
  }

}
