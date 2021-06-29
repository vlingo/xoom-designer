// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ExchangeTemplateDataFactory {

  public static List<TemplateData> build(final String exchangePackage,
                                         final List<CodeGenerationParameter> aggregates,
                                         final List<Content> contents) {
    final Supplier<Stream<CodeGenerationParameter>> filteredAggregates = () ->
            aggregates.stream().filter(aggregate -> aggregate.hasAny(Label.EXCHANGE));

    final List<Exchange> exchanges =
            Exchange.from(filteredAggregates.get().collect(toList()));

    final List<TemplateData> mappers =
            ExchangeMapperTemplateData.from(exchangePackage, filteredAggregates.get());

    final List<TemplateData> holders =
            ExchangeReceiverHolderTemplateData.from(exchangePackage, filteredAggregates.get(), contents);

    final List<TemplateData> adapters =
            ExchangeAdapterTemplateData.from(exchangePackage, filteredAggregates.get());

    final List<TemplateData> properties =
            Arrays.asList(ExchangePropertiesTemplateData.from(exchanges));

    final List<TemplateData> dispatcher =
            Arrays.asList(ExchangeDispatcherTemplateData.from(exchangePackage, filteredAggregates.get(), contents));

    final List<TemplateData> bootstrap =
            Arrays.asList(ExchangeBootstrapTemplateData.from(exchangePackage, exchanges));

    return Stream.of(mappers, holders, adapters, properties, dispatcher, bootstrap).flatMap(List::stream).collect(toList());
  }
}
