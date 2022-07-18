// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeReceiverHolderTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String exchangePackage, final Stream<CodeGenerationParameter> aggregates,
                                        List<CodeGenerationParameter> valueObjects, final List<Content> contents) {
    return aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(Label.EXCHANGE))
            .filter(exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isConsumer())
            .map(exchange -> new ExchangeReceiverHolderTemplateData(exchangePackage, exchange, valueObjects, contents))
            .collect(Collectors.toList());
  }

  private ExchangeReceiverHolderTemplateData(final String exchangePackage, final CodeGenerationParameter exchange,
                                             List<CodeGenerationParameter> valueObjects, final List<Content> contents) {
    final List<ExchangeReceiver> receiversParameters = ExchangeReceiver.from(exchange, valueObjects);
    this.parameters =
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, exchangePackage)
                    .and(TemplateParameter.EXCHANGE_RECEIVERS, receiversParameters)
                    .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, exchange.parent().value)
                    .andResolve(TemplateParameter.EXCHANGE_RECEIVER_HOLDER_NAME, params -> standard().resolveClassname(params))
                    .addImports(resolveImports(exchange, receiversParameters, contents, valueObjects));
  }

  private Set<String> resolveImports(final CodeGenerationParameter exchange, final List<ExchangeReceiver> receiversParameters,
                                     final List<Content> contents, List<CodeGenerationParameter> valueObjects) {
    final Set<String> imports = new HashSet<>();
    final String aggregateName = exchange.parent().value;
    final boolean involvesActorLoad =
            receiversParameters.stream().anyMatch(receiver -> !receiver.dispatchToFactoryMethod);

    if (involvesActorLoad) {
      final String aggregateEntityName = JavaTemplateStandard.AGGREGATE.resolveClassname(aggregateName);
      imports.add(ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.AGGREGATE, aggregateEntityName, contents));
      imports.add(Definition.class.getCanonicalName());
    }

    imports.add(ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.AGGREGATE_PROTOCOL, aggregateName, contents));
    imports.addAll(receiversParameters.stream().map(receiver -> receiver.localTypeQualifiedName).collect(Collectors.toSet()));
    imports.addAll(receiversParameters.stream().map(receiver -> receiver.localTypeQualifiedName).collect(Collectors.toSet()));
    imports.addAll(valueObjects.stream().map(vo -> ValueObjectDetail.resolveTypeImport(vo.value, contents)).collect(Collectors.toSet()));
    return imports;
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
