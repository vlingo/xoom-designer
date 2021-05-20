// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.autodispatch;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.language.Language;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.QUERIES_ACTOR;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.VALUE_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.ROUTE_METHOD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class AutoDispatchMappingGenerationStepTest {

    @Test
    public void testThatAutoDispatchMappingsAreGenerated() throws IOException {
        final String basePackage = "io.vlingo.xoomapp";

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, basePackage),
                        CodeGenerationParameter.of(CQRS, true), CodeGenerationParameter.of(LANGUAGE, Language.JAVA),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        new AutoDispatchMappingGenerationStep().process(context);

        final Content authorMappingContent = context.findContent(AUTO_DISPATCH_MAPPING, "AuthorResource");
        final Content authorHandlersMappingContent = context.findContent(AUTO_DISPATCH_HANDLERS_MAPPING, "AuthorResourceHandlers");

        Assertions.assertEquals(16, context.contents().size());
        Assertions.assertTrue(authorMappingContent.contains(TextExpectation.onJava().read("author-dispatch-mapping")));
        Assertions.assertTrue(authorHandlersMappingContent.contains(TextExpectation.onJava().read("author-dispatch-handlers-mapping")));
    }

    private Content[] contents() {
        return new Content[]{
                Content.with(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
                Content.with(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), null, null, BOOK_CONTENT_TEXT),
                Content.with(AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.java"), null, null, AUTHOR_AGGREGATE_CONTENT_TEXT),
                Content.with(AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookEntity.java"), null, null, BOOK_AGGREGATE_CONTENT_TEXT),
                Content.with(AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                Content.with(AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.java"), null, null, BOOK_STATE_CONTENT_TEXT),
                Content.with(DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
                Content.with(DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.java"), null, null, BOOK_DATA_CONTENT_TEXT),
                Content.with(VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueries.java"), null, null, AUTHOR_QUERIES_CONTENT_TEXT),
                Content.with(QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "BookQueries.java"), null, null, BOOK_QUERIES_CONTENT_TEXT),
                Content.with(QUERIES_ACTOR, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueriesActor.java"), null, null, AUTHOR_QUERIES_ACTOR_CONTENT_TEXT),
                Content.with(QUERIES_ACTOR, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "BookQueriesActor.java"), null, null, BOOK_QUERIES_ACTOR_CONTENT_TEXT)
        };
    }

    private CodeGenerationParameter authorAggregate() {
        final CodeGenerationParameter idField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter nameField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "Name");

        final CodeGenerationParameter rankField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
                        .relate(Label.FIELD_TYPE, "Rank");

        final CodeGenerationParameter authorRegisteredEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                        .relate(idField).relate(nameField);

        final CodeGenerationParameter authorRankedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                        .relate(idField).relate(rankField);

        final CodeGenerationParameter factoryMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                        .relate(Label.METHOD_PARAMETER, "name")
                        .relate(FACTORY_METHOD, "true")
                        .relate(authorRegisteredEvent);

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank")
                        .relate(authorRankedEvent);

        final CodeGenerationParameter withNameRoute =
                CodeGenerationParameter.of(ROUTE_SIGNATURE, "withName")
                        .relate(ROUTE_METHOD, "POST")
                        .relate(ROUTE_PATH, "/")
                        .relate(REQUIRE_ENTITY_LOADING, "false");

        final CodeGenerationParameter changeRankRoute =
                CodeGenerationParameter.of(ROUTE_SIGNATURE, "changeRank")
                        .relate(ROUTE_METHOD, "PATCH")
                        .relate(ROUTE_PATH, "/{id}/rank")
                        .relate(REQUIRE_ENTITY_LOADING, "true");

        return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                .relate(URI_ROOT, "/authors").relate(idField)
                .relate(nameField).relate(rankField).relate(factoryMethod)
                .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
                .relate(authorRegisteredEvent).relate(authorRankedEvent);
    }

    private CodeGenerationParameter nameValueObject() {
        return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
                        .relate(Label.FIELD_TYPE, "String"))
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
                        .relate(Label.FIELD_TYPE, "String"));
    }

    private CodeGenerationParameter rankValueObject() {
        return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Rank")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "points")
                        .relate(Label.FIELD_TYPE, "int"))
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classification")
                        .relate(Label.FIELD_TYPE, "String"));
    }

    private static final String PROJECT_PATH =
            OperatingSystem.detect().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    private static final String INFRASTRUCTURE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure").toString();

    private static final String PERSISTENCE_PACKAGE_PATH =
            Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

    private static final String AUTHOR_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public interface Author { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public interface Book { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorState { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public class BookState { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_AGGREGATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorEntity { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_AGGREGATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public class BookEntity { \\n" +
                    "... \\n" +
                    "}";

    private static final String NAME_VALUE_OBJECT_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class Name { \\n" +
                    "... \\n" +
                    "}";

    private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class Rank { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_DATA_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure; \\n" +
                    "public class AuthorData { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_DATA_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure; \\n" +
                    "public class BookData { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_QUERIES_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public interface AuthorQueries { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_QUERIES_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public interface BookQueries { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_QUERIES_ACTOR_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class AuthorQueriesActor { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_QUERIES_ACTOR_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class BookQueriesActor { \\n" +
                    "... \\n" +
                    "}";
}
