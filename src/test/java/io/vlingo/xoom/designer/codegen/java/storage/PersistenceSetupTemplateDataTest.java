// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.*;

public class PersistenceSetupTemplateDataTest extends CodeGenerationTest {

    @Test
    public void testWithAdaptersAndProjections() {
        final List<TemplateData> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", "xoomapp", contents(),
                        StorageType.STATE_STORE, databaseTypes(), ProjectionType.EVENT_BASED, true, true);

        //General Assert
        Assertions.assertEquals(9, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.QUERIES)).count());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.QUERIES_ACTOR)).count());
        Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.PERSISTENCE_SETUP)).count());
        Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).count());

        //Assert for Queries
        final TemplateData queriesTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.QUERIES)).findFirst().get();

        final TemplateParameters queriesParameters =
                queriesTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, queriesParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("BookQueries", queriesParameters.find(TemplateParameter.QUERIES_NAME));
        Assertions.assertEquals("BookData", queriesParameters.find(TemplateParameter.STATE_DATA_OBJECT_NAME));
        Assertions.assertEquals("bookOf", queriesParameters.find(TemplateParameter.QUERY_BY_ID_METHOD_NAME));
        Assertions.assertEquals("books", queriesParameters.find(TemplateParameter.QUERY_ALL_METHOD_NAME));
        Assertions.assertEquals(1, queriesParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(queriesParameters.hasImport("io.vlingo.xoomapp.infrastructure.BookData"));

        //Assert for QueriesActor
        final TemplateData queriesActorTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.QUERIES_ACTOR)).findFirst().get();

        final TemplateParameters queriesActorParameters =
                queriesActorTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, queriesActorParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("BookQueriesActor", queriesActorParameters.find(TemplateParameter.QUERIES_ACTOR_NAME));

        //Assert for StoreProvider
        final TemplateData storeProviderData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).findFirst().get();

        Assertions.assertTrue(storeProviderData.isPlaceholder());

        //Assert for PersistenceSetup
        final TemplateData persistenceSetupData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.PERSISTENCE_SETUP)).findFirst().get();

        final TemplateParameters persistenceSetupParameters = persistenceSetupData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, persistenceSetupParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp", persistenceSetupParameters.find(TemplateParameter.BASE_PACKAGE));
        Assertions.assertEquals(7, persistenceSetupParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorState"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.model.book.BookState"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorRated"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.model.book.BookRented"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.model.book.BookPurchased"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.infrastructure.AuthorData"));
        Assertions.assertTrue(persistenceSetupParameters.hasImport("io.vlingo.xoomapp.infrastructure.BookData"));

        Assertions.assertEquals("BookState", persistenceSetupParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
        Assertions.assertFalse(persistenceSetupParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).isLast());
        Assertions.assertEquals("AuthorState", persistenceSetupParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
        Assertions.assertTrue(persistenceSetupParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).isLast());
        Assertions.assertEquals("AuthorProjectionActor", persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(0).getActor());
        Assertions.assertEquals("AuthorRated.class", persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(0).getCauses());
        Assertions.assertFalse(persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(0).isLast());
        Assertions.assertEquals("BookProjectionActor", persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(1).getActor());
        Assertions.assertEquals("BookRented.class, BookPurchased.class", persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(1).getCauses());
        Assertions.assertTrue(persistenceSetupParameters.<List<Projection>>find(TemplateParameter.PROJECTIONS).get(1).isLast());
        Assertions.assertEquals("PersistenceSetup", persistenceSetupData.filename());
        Assertions.assertEquals("AuthorData.class, BookData.class", persistenceSetupParameters.find(TemplateParameter.DATA_OBJECTS));
        Assertions.assertTrue((Boolean) persistenceSetupParameters.find(TemplateParameter.REQUIRE_ADAPTERS));
        Assertions.assertTrue((Boolean) persistenceSetupParameters.find(TemplateParameter.USE_PROJECTIONS));
        Assertions.assertTrue((Boolean) persistenceSetupParameters.find(TemplateParameter.USE_ANNOTATIONS));
        Assertions.assertTrue((Boolean) persistenceSetupParameters.find(TemplateParameter.USE_CQRS));
    }

    private List<Content> contents() {
        return Arrays.asList(
                Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRated.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.java"), null, null, BOOK_STATE_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookRented.java"), null, null, BOOK_RENTED_TEXT),
                Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookPurchased.java"), null, null, BOOK_PURCHASED_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), null, null, BOOK_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
                Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.java"), null, null, BOOK_DATA_CONTENT_TEXT)
            );
    }

    private static final Map<Model, DatabaseType> databaseTypes() {
        return new HashMap<Model, DatabaseType>() {
          private static final long serialVersionUID = 1L;
          {
            put(Model.COMMAND, DatabaseType.HSQLDB);
            put(Model.QUERY, DatabaseType.IN_MEMORY);
        }};
    }

    private static final String EXPECTED_PACKAGE = "io.vlingo.xoomapp.infrastructure.persistence";

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

    private static final String BOOK_RENTED_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public class BookRented extends Event { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_PURCHASED_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public class BookPurchased extends Event { \\n" +
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
}
