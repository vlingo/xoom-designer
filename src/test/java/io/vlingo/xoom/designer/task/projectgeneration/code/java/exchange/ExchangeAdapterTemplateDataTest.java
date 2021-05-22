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
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ExchangeAdapterTemplateDataTest {

    @Test
    public void testThatTemplateParametersAreMapped() {
        final List<TemplateData> data =
                ExchangeAdapterTemplateData.from("io.vlingo.xoomapp.infrastructure.exchange",
                        CodeGenerationParametersBuilder.threeExchanges(),
                        Arrays.asList(ContentBuilder.authorDataObjectContent()));

        Assertions.assertEquals(3, data.size());

        final Predicate<TemplateParameters> filterAuthorConsumerAdapter =
                parameters -> parameters.find(TemplateParameter.EXCHANGE_ADAPTER_NAME).equals("AuthorConsumerAdapter");

        final TemplateParameters authorConsumerAdapterParameters =
                data.stream().map(TemplateData::parameters).filter(filterAuthorConsumerAdapter).findFirst().get();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", authorConsumerAdapterParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("Author", authorConsumerAdapterParameters.find(TemplateParameter.AGGREGATE_PROTOCOL_NAME));
        Assertions.assertEquals("AuthorData", authorConsumerAdapterParameters.find(TemplateParameter.LOCAL_TYPE_NAME));
        Assertions.assertEquals("CONSUMER", authorConsumerAdapterParameters.<ExchangeRole>find(TemplateParameter.EXCHANGE_ROLE).name());
        Assertions.assertEquals("AuthorDataMapper", authorConsumerAdapterParameters.find(TemplateParameter.EXCHANGE_MAPPER_NAME));
        Assertions.assertTrue(authorConsumerAdapterParameters.hasImport("io.vlingo.xoomapp.infrastructure.AuthorData"));

        final Predicate<TemplateParameters> filterBookProducerAdapter =
                parameters -> parameters.find(TemplateParameter.EXCHANGE_ADAPTER_NAME).equals("BookProducerAdapter");

        final TemplateParameters bookProduceAdapterParameters =
                data.stream().map(TemplateData::parameters).filter(filterBookProducerAdapter).findFirst().get();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", bookProduceAdapterParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("Book", bookProduceAdapterParameters.find(TemplateParameter.AGGREGATE_PROTOCOL_NAME));
        Assertions.assertEquals("vlingo:xoom:io.vlingo.xoomapp", bookProduceAdapterParameters.find(TemplateParameter.SCHEMA_GROUP_NAME));
        Assertions.assertEquals("PRODUCER", bookProduceAdapterParameters.<ExchangeRole>find(TemplateParameter.EXCHANGE_ROLE).name());
        Assertions.assertTrue(bookProduceAdapterParameters.hasImport(IdentifiedDomainEvent.class.getCanonicalName()));
    }

}
