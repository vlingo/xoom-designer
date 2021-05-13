// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExchangePropertiesTemplateDataTest {

    @Test
    @SuppressWarnings("rawtypes")
    public void testThatTemplateParametersAreMapped() {
        final TemplateData data =
                ExchangePropertiesTemplateData.from(CodeGenerationParametersBuilder.threeExchanges());

        final TemplateParameters parameters = data.parameters();
        Assertions.assertTrue((Boolean) parameters.find(TemplateParameter.RESOURCE_FILE));
        Assertions.assertTrue(parameters.<List>find(TemplateParameter.EXCHANGE_NAMES).contains("otherapp-exchange"));
        Assertions.assertTrue(parameters.<List>find(TemplateParameter.EXCHANGE_NAMES).contains("author-exchange"));
        Assertions.assertTrue(parameters.<List>find(TemplateParameter.EXCHANGE_NAMES).contains("book-exchange"));
        Assertions.assertEquals("otherapp-exchange;author-exchange;book-exchange", parameters.find(TemplateParameter.INLINE_EXCHANGE_NAMES));
    }
}
