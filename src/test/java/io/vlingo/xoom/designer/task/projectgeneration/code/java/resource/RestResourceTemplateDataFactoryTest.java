// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.resource;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.Queries;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RestResourceTemplateDataFactoryTest {

    @Test
    public void testThatRestResourceIsGeneratedWithCQRS() {
        final CodeGenerationParameter packageParameter =
                CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp");

        final CodeGenerationParameter useCQRSParameter =
                CodeGenerationParameter.of(Label.CQRS, "true");

        final CodeGenerationParameter dialect =
                CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA);

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(packageParameter, dialect,
                        useCQRSParameter, authorAggregate());

        final List<TemplateData> templatesData =
                RestResourceTemplateDataFactory.build(parameters, contents());

        Assertions.assertEquals(1, templatesData.size());

        final TemplateParameters templateParameters = templatesData.get(0).parameters();

        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.model.author.Author"));
        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorEntity"));
        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.infrastructure.*"));
        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.infrastructure.persistence.AuthorQueries"));
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.resource", templateParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("AuthorResource", templateParameters.find(TemplateParameter.REST_RESOURCE_NAME));
        Assertions.assertEquals("Author", templateParameters.find(TemplateParameter.MODEL_PROTOCOL));
        Assertions.assertEquals("AuthorEntity", templateParameters.find(TemplateParameter.MODEL_ACTOR));
        Assertions.assertEquals("QueryModelStateStoreProvider", templateParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
        Assertions.assertEquals("/authors", templateParameters.find(TemplateParameter.URI_ROOT));
        Assertions.assertEquals(true, templateParameters.find(TemplateParameter.USE_CQRS));

        final Queries queries =
                templateParameters.find(TemplateParameter.QUERIES);

        Assertions.assertEquals("AuthorQueries", queries.getProtocolName());
        Assertions.assertEquals("AuthorQueriesActor", queries.getActorName());
        Assertions.assertEquals("authorQueries", queries.getAttributeName());

        final List<RouteDeclaration> routeDeclarations =
                templateParameters.find(TemplateParameter.ROUTE_DECLARATIONS);

        final RouteDeclaration nameUpdateRouteDeclaration =
                routeDeclarations.stream().filter(parameter ->
                        parameter.getHandlerName().equals("withName"))
                        .findFirst().get();

        Assertions.assertEquals("AuthorData", nameUpdateRouteDeclaration.getBodyType());
        Assertions.assertEquals("io.vlingo.xoom.http.resource.ResourceBuilder.post", nameUpdateRouteDeclaration.getBuilderMethod());
        Assertions.assertEquals("/authors", nameUpdateRouteDeclaration.getPath());
    }

    @Test
    public void testThatRestResourceIsGeneratedWithoutCQRS() {
        final CodeGenerationParameter packageParameter =
                CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp");

        final CodeGenerationParameter useCQRSParameter =
                CodeGenerationParameter.of(Label.CQRS, "false");

        final CodeGenerationParameter dialect =
                CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA);

        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(packageParameter, dialect,
                        useCQRSParameter, authorAggregate());

        final List<TemplateData> templatesData =
                RestResourceTemplateDataFactory.build(parameters, contents());

        Assertions.assertEquals(1, templatesData.size());

        final TemplateParameters templateParameters = templatesData.get(0).parameters();

        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.model.author.Author"));
        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorEntity"));
        Assertions.assertTrue(templateParameters.hasImport("io.vlingo.xoomapp.infrastructure.*"));
        Assertions.assertFalse(templateParameters.hasImport("io.vlingo.xoomapp.infrastructure.persistence.AuthorQueries"));
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.resource", templateParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("AuthorResource", templateParameters.find(TemplateParameter.REST_RESOURCE_NAME));
        Assertions.assertEquals("Author", templateParameters.find(TemplateParameter.MODEL_PROTOCOL));
        Assertions.assertEquals("AuthorEntity", templateParameters.find(TemplateParameter.MODEL_ACTOR));
        Assertions.assertEquals("/authors", templateParameters.find(TemplateParameter.URI_ROOT));
        Assertions.assertEquals(false, templateParameters.find(TemplateParameter.USE_CQRS));

        final Queries queries =
                templateParameters.find(TemplateParameter.QUERIES);

        Assertions.assertTrue(queries.isEmpty());

        final List<RouteDeclaration> routeDeclarations =
                templateParameters.find(TemplateParameter.ROUTE_DECLARATIONS);

        final RouteDeclaration nameUpdateRouteDeclaration =
                routeDeclarations.stream().filter(parameter ->
                        parameter.getHandlerName().equals("withName"))
                        .findFirst().get();

        Assertions.assertEquals("AuthorData", nameUpdateRouteDeclaration.getBodyType());
        Assertions.assertEquals("io.vlingo.xoom.http.resource.ResourceBuilder.post", nameUpdateRouteDeclaration.getBuilderMethod());
        Assertions.assertEquals("/authors", nameUpdateRouteDeclaration.getPath());
    }

    private CodeGenerationParameter authorAggregate() {
        final CodeGenerationParameter idField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter nameField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter rankField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
                        .relate(Label.FIELD_TYPE, "int");

        final CodeGenerationParameter authorRegisteredEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                        .relate(idField).relate(nameField);

        final CodeGenerationParameter authorRankedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                        .relate(idField).relate(rankField);

        final CodeGenerationParameter factoryMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                        .relate(Label.METHOD_PARAMETER, "name")
                        .relate(Label.FACTORY_METHOD, "true")
                        .relate(authorRegisteredEvent);

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank")
                        .relate(authorRankedEvent);

        final CodeGenerationParameter withNameRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
                        .relate(Label.ROUTE_METHOD, "POST")
                        .relate(Label.ROUTE_PATH, "/")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "false");

        final CodeGenerationParameter changeRankRoute =
                CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "changeRank")
                        .relate(Label.ROUTE_METHOD, "PATCH")
                        .relate(Label.ROUTE_PATH, "/{id}/rank")
                        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

        return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                .relate(Label.URI_ROOT, "/authors").relate(idField)
                .relate(nameField).relate(rankField).relate(factoryMethod)
                .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
                .relate(authorRegisteredEvent).relate(authorRankedEvent);
    }

    private List<Content> contents() {
        return Arrays.asList(
                Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.java"), null, null, AUTHOR_AGGREGATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueries.java"), null, null, AUTHOR_QUERIES_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.QUERIES_ACTOR, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueriesActor.java"), null, null, AUTHOR_QUERIES_ACTOR_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.STORE_PROVIDER, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "QueryModelStateStoreProvider.java"), null, null, QUERY_MODEL_STORE_PROVIDER_CONTENT)
        );
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
