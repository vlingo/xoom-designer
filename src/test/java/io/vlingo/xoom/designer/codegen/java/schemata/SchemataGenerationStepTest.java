// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.SchemataSettings;
import io.vlingo.xoom.designer.codegen.java.exchange.CodeGenerationParametersBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class SchemataGenerationStepTest extends CodeGenerationTest {

    @Test
    public void testThatSchemataResourcesAreGenerated() {
        final SchemataSettings schemataSettings =
                SchemataSettings.with("localhost", 18787, Optional.of(Tuple2.from("xoom-schemata", 1009)));

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.empty()
                        .addAll(CodeGenerationParametersBuilder.threeExchanges().collect(toList()))
                        .add(CodeGenerationParameter.ofObject(Label.SCHEMATA_SETTINGS, schemataSettings));

        final CodeGenerationContext context = CodeGenerationContext.with(parameters);

        new SchemataGenerationStep().process(context);

        final Content plugin =
                context.findContent(JavaTemplateStandard.SCHEMATA_PLUGIN, "pom");

        final Content dns =
                context.findContent(JavaTemplateStandard.SCHEMATA_DNS, "pom");

        final Content authorRatedSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "AuthorRated");

        final Content authorBlockedSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "AuthorBlocked");

        final Content bookSoldOutSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "BookSoldOut");

        final Content bookPurchasedSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "BookPurchased");

        final Content nameSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "Name");

        final Content rankSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "Rank");

        final Content classificationSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "Classification");

        final Content classifierSpecification =
                context.findContent(JavaTemplateStandard.SCHEMATA_SPECIFICATION, "Classifier");

        Assertions.assertEquals(10, context.contents().size());
        Assertions.assertTrue(plugin.contains(TextExpectation.onJava().read("schemata-plugin")));
        Assertions.assertTrue(dns.contains(TextExpectation.onJava().read("schemata-dns")));
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
