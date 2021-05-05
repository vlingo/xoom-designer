// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.turbo.codegen.template.TemplateParameter.*;

public class ExchangeBootstrapTemplateDataTest {

    @Test
    public void testThatTemplateParametersAreMapped() {
        final TemplateData data =
                ExchangeBootstrapTemplateData.from("io.vlingo.xoomapp.infrastructure.exchange",
                        CodeGenerationParametersBuilder.threeExchanges(),
                        Arrays.asList(ContentBuilder.authorDataObjectContent()));

        final TemplateParameters parameters = data.parameters();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", parameters.find(PACKAGE_NAME));
        Assertions.assertEquals("authorExchange, bookExchange", parameters.find(PRODUCER_EXCHANGES));

        final List<Exchange> exchanges = data.parameters().find(EXCHANGES);

        Assertions.assertEquals(3, exchanges.size());
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.getName().equals("author-exchange")));
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.getName().equals("book-exchange")));
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.getName().equals("otherapp-exchange")));

        final Exchange otherAppExchange =
                exchanges.stream().filter(param -> param.getName().equals("otherapp-exchange")).findFirst().get();

        Assertions.assertEquals("otherappExchange", otherAppExchange.getVariableName());
        Assertions.assertEquals("otherappExchangeSettings", otherAppExchange.getSettingsName());
        Assertions.assertEquals(3, otherAppExchange.getCoveys().size());
        Assertions.assertTrue(otherAppExchange.getCoveys().stream().allMatch(covey -> covey.getLocalClass().equals("AuthorData")));
        Assertions.assertTrue(otherAppExchange.getCoveys().stream().allMatch(convey -> convey.getExternalClass().equals("String")));

        final CoveyParameter schemaRemovedCovey =
                otherAppExchange.getCoveys().stream()
                        .filter(convey -> convey.getReceiverInstantiation().equals("new AuthorExchangeReceivers.OtherAggregateRemoved(stage)"))
                        .findFirst().get();

        Assertions.assertEquals("new AuthorConsumerAdapter(\"vlingo:xoom:io.vlingo.xoom.otherapp:OtherAggregateRemoved:0.0.3\")", schemaRemovedCovey.getAdapterInstantiation());

        final Exchange authorExchange =
                exchanges.stream().filter(param -> param.getName().equals("author-exchange")).findFirst().get();

        Assertions.assertEquals("authorExchange", authorExchange.getVariableName());
        Assertions.assertEquals("authorExchangeSettings", authorExchange.getSettingsName());
        Assertions.assertEquals(1, authorExchange.getCoveys().size());

        final CoveyParameter authorExchangeCovey = authorExchange.getCoveys().stream().findFirst().get();

        Assertions.assertEquals("IdentifiedDomainEvent", authorExchangeCovey.getLocalClass());
        Assertions.assertEquals("IdentifiedDomainEvent", authorExchangeCovey.getExternalClass());
        Assertions.assertEquals("new AuthorProducerAdapter()",authorExchangeCovey.getAdapterInstantiation());
        Assertions.assertEquals("received -> {}", authorExchangeCovey.getReceiverInstantiation());
    }

}
