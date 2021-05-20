// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.PACKAGE;
import static java.util.stream.Collectors.toList;

public class ExchangeGenerationStepTest {

    @Test
    public void testThatExchangeCodeIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.empty()
                        .addAll(CodeGenerationParametersBuilder.threeExchanges().collect(toList()));

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).with(PACKAGE, "io.vlingo.xoomapp");

        context.contents(ContentBuilder.contents().toArray(new Content[]{}));

        new ExchangeGenerationStep().process(context);

        Assertions.assertEquals(19, context.contents().size());

        final Content exchangeBootstrap =
                context.findContent(DesignerTemplateStandard.EXCHANGE_BOOTSTRAP, "ExchangeBootstrap");

        final Content authorExchangeReceivers =
                context.findContent(DesignerTemplateStandard.EXCHANGE_RECEIVER_HOLDER, "AuthorExchangeReceivers");

        final Content authorConsumerAdapter =
                context.findContent(DesignerTemplateStandard.EXCHANGE_ADAPTER, "AuthorConsumerAdapter");

        final Content authorProducerAdapter =
                context.findContent(DesignerTemplateStandard.EXCHANGE_ADAPTER, "AuthorProducerAdapter");

        final Content authorDataMapper =
                context.findContent(DesignerTemplateStandard.EXCHANGE_MAPPER, "AuthorDataMapper");

        final Content bookProducerAdapter =
                context.findContent(DesignerTemplateStandard.EXCHANGE_ADAPTER, "BookProducerAdapter");

        final Content domainEventMapper =
                context.findContent(DesignerTemplateStandard.EXCHANGE_MAPPER, "DomainEventMapper");

        final Content exchangeDispatcher =
                context.findContent(DesignerTemplateStandard.EXCHANGE_DISPATCHER, "ExchangeDispatcher");

        exchangeBootstrap.contains(TextExpectation.onJava().read("exchange-bootstrap"));
        authorExchangeReceivers.contains(TextExpectation.onJava().read("author-exchange-receivers"));
        authorConsumerAdapter.contains(TextExpectation.onJava().read("author-consumer-adapter"));
        authorProducerAdapter.contains(TextExpectation.onJava().read("author-producer-adapter"));
        authorDataMapper.contains(TextExpectation.onJava().read("author-data-mapper"));
        bookProducerAdapter.contains(TextExpectation.onJava().read("book-producer-adapter"));
        domainEventMapper.contains(TextExpectation.onJava().read("domain-event-mapper"));
        exchangeDispatcher.contains(TextExpectation.onJava().read("exchange-dispatcher"));
    }
}
