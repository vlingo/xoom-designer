// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeBootstrapTemplateDataTest {

    @Test
    public void testThatTemplateParametersAreMapped() {
        final List<CodeGenerationParameter> exchangesParams =
                CodeGenerationParametersBuilder.threeExchanges().collect(Collectors.toList());

        final TemplateData data =
                ExchangeBootstrapTemplateData.from("io.vlingo.xoomapp.infrastructure.exchange",
                        Exchange.from(5877, exchangesParams), Arrays.asList(ContentBuilder.authorDataObjectContent()));

        final TemplateParameters parameters = data.parameters();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", parameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("bookExchange, authorExchange", parameters.find(TemplateParameter.PRODUCER_EXCHANGES));

        final List<Exchange> exchanges = data.parameters().find(TemplateParameter.EXCHANGES);

        Assertions.assertEquals(3, exchanges.size());
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.name.equals("author-exchange")));
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.name.equals("book-exchange")));
        Assertions.assertTrue(exchanges.stream().anyMatch(param -> param.name.equals("otherapp-exchange")));

        final Exchange otherAppExchange =
                exchanges.stream().filter(param -> param.name.equals("otherapp-exchange")).findFirst().get();

        Assertions.assertEquals("otherappExchange", otherAppExchange.variableName);
        Assertions.assertEquals("otherappExchangeSettings", otherAppExchange.settingsName);
        Assertions.assertEquals(3, otherAppExchange.coveys.size());
        Assertions.assertTrue(otherAppExchange.coveys.stream().allMatch(covey -> covey.getLocalClass().equals("AuthorData")));
        Assertions.assertTrue(otherAppExchange.coveys.stream().allMatch(convey -> convey.getExternalClass().equals("String")));

        final CoveyParameter schemaRemovedCovey =
                otherAppExchange.coveys.stream()
                        .filter(convey -> convey.getReceiverInstantiation().equals("new AuthorExchangeReceivers.OtherAggregateRemoved(stage)"))
                        .findFirst().get();

        Assertions.assertEquals("new AuthorConsumerAdapter(\"vlingo:xoom:io.vlingo.xoom.otherapp:OtherAggregateRemoved:0.0.3\")", schemaRemovedCovey.getAdapterInstantiation());

        final Exchange authorExchange =
                exchanges.stream().filter(param -> param.name.equals("author-exchange")).findFirst().get();

        Assertions.assertEquals("authorExchange", authorExchange.variableName);
        Assertions.assertEquals("authorExchangeSettings", authorExchange.settingsName);
        Assertions.assertEquals(1, authorExchange.coveys.size());

        final CoveyParameter authorExchangeCovey = authorExchange.coveys.stream().findFirst().get();

        Assertions.assertEquals("IdentifiedDomainEvent", authorExchangeCovey.getLocalClass());
        Assertions.assertEquals("IdentifiedDomainEvent", authorExchangeCovey.getExternalClass());
        Assertions.assertEquals("new AuthorProducerAdapter()",authorExchangeCovey.getAdapterInstantiation());
        Assertions.assertEquals("received -> {}", authorExchangeCovey.getReceiverInstantiation());
    }

}
