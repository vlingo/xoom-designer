// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.TextBasedContent;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatExchangeCodeIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.empty()
        .addAll(CodeGenerationParametersBuilder.threeExchanges().collect(toList()));

    final CodeGenerationContext context = CodeGenerationContext.with(parameters)
        .with(Label.PACKAGE, "Io.Vlingo.Xoomapp");

    context.contents(ContentBuilder.contents().toArray(new Content[]{}));

    new ExchangeGenerationStep().process(context);

    Assertions.assertEquals(25, context.contents().size());

    final Content exchangeBootstrap =
        context.findContent(CsharpTemplateStandard.EXCHANGE_BOOTSTRAP, "ExchangeBootstrap");

    final Content authorExchangeReceivers =
        context.findContent(CsharpTemplateStandard.EXCHANGE_RECEIVER_HOLDER, "AuthorExchangeReceivers");

    final Content otherAggregateDefinedAdapter =
        context.findContent(CsharpTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateDefinedAdapter");

    final Content otherAggregateUpdatedAdapter =
        context.findContent(CsharpTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateUpdatedAdapter");

    final Content otherAggregateRemovedAdapter =
        context.findContent(CsharpTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateRemovedAdapter");

    final Content authorProducerAdapter =
        context.findContent(CsharpTemplateStandard.EXCHANGE_ADAPTER, "AuthorProducerAdapter");

    final Content otherAggregateDefinedMapper =
        context.findContent(CsharpTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateDefinedMapper");

    final Content otherAggregateUpdatedMapper =
        context.findContent(CsharpTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateUpdatedMapper");

    final Content otherAggregateRemovedMapper =
        context.findContent(CsharpTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateRemovedMapper");

    final Content bookProducerAdapter =
        context.findContent(CsharpTemplateStandard.EXCHANGE_ADAPTER, "BookProducerAdapter");

    final Content domainEventMapper =
        context.findContent(CsharpTemplateStandard.EXCHANGE_MAPPER, "DomainEventMapper");

    final Content exchangeDispatcher =
        context.findContent(CsharpTemplateStandard.EXCHANGE_DISPATCHER, "ExchangeDispatcher");

    final Content exchangeProperties =
        context.findContent(CsharpTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    assertEquals(((TextBasedContent) authorExchangeReceivers).text, (TextExpectation.onCSharp().read("author-exchange-receivers")));
    assertEquals(((TextBasedContent) exchangeBootstrap).text, (TextExpectation.onCSharp().read("exchange-bootstrap")));
    assertEquals(((TextBasedContent) authorExchangeReceivers).text, (TextExpectation.onCSharp().read("author-exchange-receivers")));
    assertEquals(((TextBasedContent) otherAggregateDefinedAdapter).text, (TextExpectation.onCSharp().read("other-aggregate-defined-adapter")));
    assertEquals(((TextBasedContent) otherAggregateUpdatedAdapter).text, (TextExpectation.onCSharp().read("other-aggregate-updated-adapter")));
    assertEquals(((TextBasedContent) otherAggregateRemovedAdapter).text, (TextExpectation.onCSharp().read("other-aggregate-removed-adapter")));
    assertEquals(((TextBasedContent) authorProducerAdapter).text, (TextExpectation.onCSharp().read("author-producer-adapter")));
    assertEquals(((TextBasedContent) otherAggregateDefinedMapper).text, (TextExpectation.onCSharp().read("other-aggregate-defined-mapper")));
    assertEquals(((TextBasedContent) otherAggregateUpdatedMapper).text, (TextExpectation.onCSharp().read("other-aggregate-updated-mapper")));
    assertEquals(((TextBasedContent) otherAggregateRemovedMapper).text, (TextExpectation.onCSharp().read("other-aggregate-removed-mapper")));
    assertEquals(((TextBasedContent) bookProducerAdapter).text, (TextExpectation.onCSharp().read("book-producer-adapter")));
    assertEquals(((TextBasedContent) domainEventMapper).text, (TextExpectation.onCSharp().read("domain-event-mapper")));
    assertEquals(((TextBasedContent) exchangeDispatcher).text, (TextExpectation.onCSharp().read("exchange-dispatcher")));
    assertEquals(((TextBasedContent) exchangeProperties).text, ((TextExpectation.onCSharp().read("xoom-turbo"))));
  }
}
