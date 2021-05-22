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

import java.util.Arrays;
import java.util.List;

public class ExchangeMapperTemplateDataTest {

    @Test
    public void testThatTemplateParametersAreMapped() {
        final List<TemplateData> mappersData =
                ExchangeMapperTemplateData.from("io.vlingo.xoomapp.infrastructure.exchange",
                        CodeGenerationParametersBuilder.threeExchanges(),
                        Arrays.asList(ContentBuilder.authorDataObjectContent()));

        Assertions.assertEquals(2, mappersData.size());

        final TemplateParameters consumerMapperParameters =
                mappersData.stream().map(data -> data.parameters())
                        .filter(params -> params.find(TemplateParameter.EXCHANGE_MAPPER_NAME).equals("AuthorDataMapper"))
                        .findFirst().get();

        Assertions.assertEquals("AuthorData", consumerMapperParameters.find(TemplateParameter.LOCAL_TYPE_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", consumerMapperParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertTrue(consumerMapperParameters.hasImport("io.vlingo.xoomapp.infrastructure.AuthorData"));

        final TemplateParameters producerMapperParameters =
                mappersData.stream().map(data -> data.parameters())
                        .filter(params -> params.find(TemplateParameter.EXCHANGE_MAPPER_NAME).equals("DomainEventMapper"))
                        .findFirst().get();

        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.exchange", producerMapperParameters.find(TemplateParameter.PACKAGE_NAME));
    }

}
