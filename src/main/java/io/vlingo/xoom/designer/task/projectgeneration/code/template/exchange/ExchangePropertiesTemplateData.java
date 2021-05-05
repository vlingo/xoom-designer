// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.EXCHANGE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class ExchangePropertiesTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static TemplateData from(final Stream<CodeGenerationParameter> aggregates) {
    final List<CodeGenerationParameter> exchanges =
            aggregates.flatMap(aggregate -> aggregate.retrieveAllRelated(EXCHANGE))
                    .collect(Collectors.toList());

    final Supplier<Stream<String>> exchangeNames = () ->
            exchanges.stream().map(exchange -> exchange.value).distinct();

    return new ExchangePropertiesTemplateData(exchangeNames);
  }

  private ExchangePropertiesTemplateData(final Supplier<Stream<String>> exchangeNames) {
    this.parameters =
            TemplateParameters.with(EXCHANGE_NAMES, exchangeNames.get().collect(Collectors.toList()))
                    .and(INLINE_EXCHANGE_NAMES, exchangeNames.get().collect(Collectors.joining(";")))
                    .and(RESOURCE_FILE, true);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.EXCHANGE_PROPERTIES;
  }

}
