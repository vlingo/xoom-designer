// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.schemata;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange.CodeGenerationParametersBuilder;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.TextExpectation;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.stream.Collectors.toList;

public class SchemataGenerationStepTest {

    @Test
    public void testThatSpecificationAndPluginConfigAreGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.empty()
                        .addAll(CodeGenerationParametersBuilder.threeExchanges().collect(toList()));

        final CodeGenerationContext context = CodeGenerationContext.with(parameters);

        new SchemataGenerationStep().process(context);

        final Content authorRatedSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "AuthorRated");

        final Content authorBlockedSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "AuthorBlocked");

        final Content bookSoldOutSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "BookSoldOut");

        final Content bookPurchasedSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "BookPurchased");

        final Content nameSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "Name");

        final Content rankSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "Rank");

        final Content classificationSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "Classification");

        final Content classifierSpecification =
                context.findContent(DesignerTemplateStandard.SCHEMATA_SPECIFICATION, "Classifier");

        @SuppressWarnings("unused")
        final Content plugin =
                context.findContent(DesignerTemplateStandard.SCHEMATA_PLUGIN, "pom");

        Assertions.assertEquals(9, context.contents().size());
        Assertions.assertTrue(authorRatedSpecification.contains(TextExpectation.onJava().read("author-rated-specification")));
        Assertions.assertTrue(authorBlockedSpecification.contains(TextExpectation.onJava().read("author-blocked-specification")));
        Assertions.assertTrue(bookSoldOutSpecification.contains(TextExpectation.onJava().read("book-sold-out")));
        Assertions.assertTrue(bookPurchasedSpecification.contains(TextExpectation.onJava().read("book-purchased")));
        Assertions.assertTrue(nameSpecification.contains(TextExpectation.onJava().read("name-specification")));
        Assertions.assertTrue(rankSpecification.contains(TextExpectation.onJava().read("rank-specification")));
        Assertions.assertTrue(classificationSpecification.contains(TextExpectation.onJava().read("classification-specification")));
        Assertions.assertTrue(classifierSpecification.contains(TextExpectation.onJava().read("classifier-specification")));
    }
}
