// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExchangeDispatcherTemplateDataTest {

    @Test
    public void testThatTemplateParametersAreMapped() {
        final TemplateData dispatcherTemplateData =
                ExchangeDispatcherTemplateData.from("io.vlingo.xoomapp.infrastructure.exchange",
                        CodeGenerationParametersBuilder.threeExchanges(), ContentBuilder.contents());

        final TemplateParameters parameters = dispatcherTemplateData.parameters();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", parameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertTrue(parameters.hasImport("io.vlingo.xoomapp.model.author.AuthorRated"));
        Assertions.assertTrue(parameters.hasImport("io.vlingo.xoomapp.model.author.AuthorBlocked"));
        Assertions.assertTrue(parameters.hasImport("io.vlingo.xoomapp.model.book.BookSoldOut"));
        Assertions.assertTrue(parameters.hasImport("io.vlingo.xoomapp.model.book.BookPurchased"));

        final List<ProducerExchange> exchanges = parameters.find(TemplateParameter.PRODUCER_EXCHANGES);

        Assertions.assertEquals(2, exchanges.size());

        final ProducerExchange authorExchange =
            exchanges.stream().filter(exchange -> exchange.getName().equals("author-exchange")).findFirst().get();

        Assertions.assertEquals(2, authorExchange.getEvents().size());
        Assertions.assertTrue(authorExchange.getEvents().contains("AuthorRated"));
        Assertions.assertTrue(authorExchange.getEvents().contains("AuthorBlocked"));


    }

}
