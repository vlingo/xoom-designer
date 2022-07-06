// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.exchange;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.TextBasedContent;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExchangeGenerationStepTest extends CodeGenerationTest  {

  @Test
  public void testThatExchangeCodeIsGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.empty()
                    .addAll(CodeGenerationParametersBuilder.threeExchanges().collect(toList()));

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).with(Label.PACKAGE, "io.vlingo.xoomapp");

    context.contents(ContentBuilder.contents().toArray(new Content[]{}));

    new ExchangeGenerationStep().process(context);

    Assertions.assertEquals(25, context.contents().size());

    final Content exchangeBootstrap =
            context.findContent(JavaTemplateStandard.EXCHANGE_BOOTSTRAP, "ExchangeBootstrap");

    final Content authorExchangeReceivers =
            context.findContent(JavaTemplateStandard.EXCHANGE_RECEIVER_HOLDER, "AuthorExchangeReceivers");

    final Content otherAggregateDefinedAdapter =
            context.findContent(JavaTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateDefinedAdapter");

    final Content otherAggregateUpdatedAdapter =
            context.findContent(JavaTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateUpdatedAdapter");

    final Content otherAggregateRemovedAdapter =
            context.findContent(JavaTemplateStandard.EXCHANGE_ADAPTER, "OtherAggregateRemovedAdapter");

    final Content authorProducerAdapter =
            context.findContent(JavaTemplateStandard.EXCHANGE_ADAPTER, "AuthorProducerAdapter");

    final Content otherAggregateDefinedMapper =
            context.findContent(JavaTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateDefinedMapper");

    final Content otherAggregateUpdatedMapper =
            context.findContent(JavaTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateUpdatedMapper");

    final Content otherAggregateRemovedMapper =
            context.findContent(JavaTemplateStandard.EXCHANGE_MAPPER, "OtherAggregateRemovedMapper");

    final Content bookProducerAdapter =
            context.findContent(JavaTemplateStandard.EXCHANGE_ADAPTER, "BookProducerAdapter");

    final Content domainEventMapper =
            context.findContent(JavaTemplateStandard.EXCHANGE_MAPPER, "DomainEventMapper");

    final Content exchangeDispatcher =
            context.findContent(JavaTemplateStandard.EXCHANGE_DISPATCHER, "ExchangeDispatcher");

    final Content exchangeProperties =
            context.findContent(JavaTemplateStandard.EXCHANGE_PROPERTIES, "xoom-turbo");

    assertEquals(((TextBasedContent)authorExchangeReceivers).text, (TextExpectation.onJava().read("author-exchange-receivers")));
    assertTrue(exchangeBootstrap.contains(TextExpectation.onJava().read("exchange-bootstrap")));
    assertTrue(authorExchangeReceivers.contains(TextExpectation.onJava().read("author-exchange-receivers")));
    assertTrue(otherAggregateDefinedAdapter.contains(TextExpectation.onJava().read("other-aggregate-defined-adapter")));
    assertTrue(otherAggregateUpdatedAdapter.contains(TextExpectation.onJava().read("other-aggregate-updated-adapter")));
    assertTrue(otherAggregateRemovedAdapter.contains(TextExpectation.onJava().read("other-aggregate-removed-adapter")));
    assertTrue(authorProducerAdapter.contains(TextExpectation.onJava().read("author-producer-adapter")));
    assertTrue(otherAggregateDefinedMapper.contains(TextExpectation.onJava().read("other-aggregate-defined-mapper")));
    assertTrue(otherAggregateUpdatedMapper.contains(TextExpectation.onJava().read("other-aggregate-updated-mapper")));
    assertTrue(otherAggregateRemovedMapper.contains(TextExpectation.onJava().read("other-aggregate-removed-mapper")));
    assertTrue(bookProducerAdapter.contains(TextExpectation.onJava().read("book-producer-adapter")));
    assertTrue(domainEventMapper.contains(TextExpectation.onJava().read("domain-event-mapper")));
    assertTrue(exchangeDispatcher.contains(TextExpectation.onJava().read("exchange-dispatcher")));
    assertTrue(exchangeProperties.contains((TextExpectation.onJava().read("exchange-properties"))));
  }
}
