// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.resource;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.METHOD_PARAMETER;

public class RestResourceGenerationStepTest {

    @Test
    public void testRestResourceGeneration() throws IOException {
        final CodeGenerationParameter packageParameter =
                CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp");

        final CodeGenerationParameter useCQRSParameter =
                CodeGenerationParameter.of(Label.CQRS, "true");

        final CodeGenerationParameter dialect =
                CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA);

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(packageParameter, dialect, useCQRSParameter,
                        authorAggregate(), nameValueObject(), rankValueObject(),
                        classificationValueObject(), classifierValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        new RestResourceGenerationStep().process(context);

        final Content authorResource = context.findContent(JavaTemplateStandard.REST_RESOURCE, "AuthorResource");

        Assertions.assertEquals(11, context.contents().size());
        Assertions.assertTrue(authorResource.contains(TextExpectation.onJava().read("author-rest-resource")));
    }

    @Test
    @Disabled
    public void testRestResourceGenerationOnKotlin() {
        final CodeGenerationParameter packageParameter =
                CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp");

        final CodeGenerationParameter useCQRSParameter =
                CodeGenerationParameter.of(Label.CQRS, "true");

        final CodeGenerationParameter languageParameter =
                CodeGenerationParameter.of(Label.DIALECT, Dialect.KOTLIN);

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(packageParameter,
                        useCQRSParameter, languageParameter,
                        authorAggregate());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        new RestResourceGenerationStep().process(context);

        final List<Content> contents = context.contents();

        Assertions.assertEquals(10, contents.size());
        final Content authorResource= context.findContent(JavaTemplateStandard.REST_RESOURCE, "AuthorResource");
        Assertions.assertTrue(authorResource.contains("class AuthorResource : DynamicResourceHandler"));
        Assertions.assertTrue(authorResource.contains("Author.withName(grid, name)"));
        Assertions.assertTrue(authorResource.contains("package io.vlingo.xoomapp.infrastructure.resource"));
        Assertions.assertTrue(authorResource.contains("return stage().actorOf(Author::class.java, stage().addressFactory().from(id), Definition.has(AuthorEntity::class.java, Definition.parameters(id)))"));
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

        final CodeGenerationParameter tagsField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                        .relate(Label.FIELD_TYPE, "Tag")
                        .relate(Label.COLLECTION_TYPE, "List");

        final CodeGenerationParameter relatedAuthorsField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                        .relate(Label.FIELD_TYPE, "String")
                        .relate(Label.COLLECTION_TYPE, "Set");

        final CodeGenerationParameter availableOnField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
                        .relate(Label.FIELD_TYPE, "LocalDate");

        final CodeGenerationParameter authorRegisteredEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                        .relate(idField).relate(nameField);

        final CodeGenerationParameter authorRankedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                        .relate(idField).relate(rankField);

        final CodeGenerationParameter authorTaggedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagged")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

        final CodeGenerationParameter authorUntaggedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUntagged")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

        final CodeGenerationParameter authorRelatedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

        final CodeGenerationParameter authorUnrelatedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

        final CodeGenerationParameter factoryMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                        .relate(Label.METHOD_PARAMETER, "name")
                        .relate(Label.METHOD_PARAMETER, "availableOn")
                        .relate(Label.FACTORY_METHOD, "true")
                        .relate(authorRegisteredEvent);

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank")
                        .relate(authorRankedEvent);

        final CodeGenerationParameter withNameRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
                        .relate(Label.ROUTE_METHOD, "POST")
                        .relate(Label.ROUTE_PATH, "/authors/")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "false");

        final CodeGenerationParameter changeRankRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "changeRank")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/authors/{id}/rank")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter addTagMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTag")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                                .relate(Label.ALIAS, "tag")
                                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter addTagsMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                                .relate(Label.ALIAS, "")
                                .relate(Label.COLLECTION_MUTATION, "MERGE"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter replaceTagsMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                                .relate(Label.ALIAS, "")
                                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter removeTagMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "removeTag")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                                .relate(Label.ALIAS, "tag")
                                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
                        .relate(authorUnrelatedEvent);

        final CodeGenerationParameter relatedAuthorMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthor")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                                .relate(Label.ALIAS, "relatedAuthor")
                                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter relatedAuthorsMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthors")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                                .relate(Label.ALIAS, "")
                                .relate(Label.COLLECTION_MUTATION, "MERGE"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter relatedAuthorsReplacementMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                                .relate(Label.ALIAS, "")
                                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
                        .relate(authorRelatedEvent);

        final CodeGenerationParameter relatedAuthorRemovalMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
                        .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                                .relate(Label.ALIAS, "relatedAuthor")
                                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
                        .relate(authorUnrelatedEvent);

        final CodeGenerationParameter addTagRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTag")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/{id}/tag")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter addTagsRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTags")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/{id}/tags")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter replaceTagsRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "replaceTags")
                        .relate(Label.ROUTE_METHOD, "PUT")
                        .relate(Label.ROUTE_PATH, "/{id}/tags")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter removeTagsRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "removeTag")
                        .relate(Label.ROUTE_METHOD, "DELETE")
                        .relate(Label.ROUTE_PATH, "/{id}/tags")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter relatedAuthorRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "relateAuthor")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/{id}/related-author")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter relatedAuthorsRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "relateAuthors")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/{id}/related-authors")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter relatedAuthorsReplacementRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "replaceAllRelatedAuthors")
                        .relate(Label.ROUTE_METHOD, "PUT")
                        .relate(Label.ROUTE_PATH, "/{id}/related-authors")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        final CodeGenerationParameter relatedAuthorRemovalRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "unrelateAuthor")
                        .relate(Label.ROUTE_METHOD, "DELETE")
                        .relate(Label.ROUTE_PATH, "/{id}/related-author")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                .relate(Label.URI_ROOT, "/authors").relate(idField)
                .relate(nameField).relate(rankField).relate(availableOnField)
                .relate(tagsField).relate(relatedAuthorsField).relate(factoryMethod)
                .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
                .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(addTagMethod)
                .relate(addTagsMethod).relate(replaceTagsMethod).relate(removeTagMethod)
                .relate(relatedAuthorRoute).relate(relatedAuthorsRoute).relate(relatedAuthorsReplacementRoute)
                .relate(relatedAuthorRemovalRoute).relate(addTagRoute).relate(addTagsRoute)
                .relate(replaceTagsRoute).relate(removeTagsRoute).relate(changeRankRoute)
                .relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
                .relate(relatedAuthorsReplacementMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
                .relate(authorTaggedEvent).relate(authorUntaggedEvent);
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
                                .relate(Label.FIELD_TYPE, "Classification"));
    }

    private CodeGenerationParameter classificationValueObject() {
        return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classification")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "label")
                        .relate(Label.FIELD_TYPE, "String"))
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiers")
                        .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
    }

    private CodeGenerationParameter classifierValueObject() {
        return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "String"));

    }

    private Content[] contents() {
        return new Content[] {
                Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.java"), null, null, AUTHOR_AGGREGATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Tag.java"), null, null, TAG_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueries.java"), null, null, AUTHOR_QUERIES_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.QUERIES_ACTOR, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueriesActor.java"), null, null, AUTHOR_QUERIES_ACTOR_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.STORE_PROVIDER, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "QueryModelStateStoreProvider.java"), null, null, QUERY_MODEL_STORE_PROVIDER_CONTENT)
        };
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

    private static final String AUTHOR_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorState { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_AGGREGATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorEntity { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_DATA_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure; \\n" +
                    "public class AuthorData { \\n" +
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

    private static final String TAG_VALUE_OBJECT_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class Tag { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_QUERIES_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public interface AuthorQueries { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_QUERIES_ACTOR_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class AuthorQueriesActor { \\n" +
                    "... \\n" +
                    "}";

    private static final String QUERY_MODEL_STORE_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class QueryModelStateStoreProvider { \\n" +
                    "... \\n" +
                    "}";

}
