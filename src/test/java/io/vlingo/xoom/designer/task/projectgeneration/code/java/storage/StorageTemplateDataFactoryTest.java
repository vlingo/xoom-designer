// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationTest;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StorageTemplateDataFactoryTest extends CodeGenerationTest {

    @Test
    public void testStorageTemplateDataOnSourcedSingleModel() {
        final List<TemplateData> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", "xoomapp", contents(),
                        StorageType.JOURNAL, databaseTypes(), ProjectionType.EVENT_BASED, false, false);

        //General Assert

        Assertions.assertEquals(4, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).count());
        Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).count());

        //Assert for StateAdapter

        final TemplateData entryAdapterTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).findFirst().get();

        final TemplateParameters stateAdapterConfigurationParameters =
                entryAdapterTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("BookRented", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
        Assertions.assertEquals(StorageType.JOURNAL, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("io.vlingo.xoomapp.model.book.BookRented"));
        Assertions.assertEquals("BookRentedAdapter", entryAdapterTemplateData.filename());

        //Assert for StoreProvider

        final TemplateData storeProviderTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).findFirst().get();

        final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals(Model.DOMAIN, storeProviderParameters.find(TemplateParameter.MODEL));
        Assertions.assertEquals("JournalProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
        Assertions.assertEquals(4, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.book.BookRented"));
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.book.BookPurchased"));
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorEntity"));
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.book.BookEntity"));
        Assertions.assertEquals("BookRented", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
        Assertions.assertEquals("BookRentedAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
        Assertions.assertEquals("BookPurchased", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
        Assertions.assertEquals("BookPurchasedAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
        Assertions.assertEquals(2, storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).size());
        Assertions.assertTrue(storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).contains("AuthorEntity"));
        Assertions.assertTrue(storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).contains("BookEntity"));
        Assertions.assertEquals("JournalProvider", storeProviderTemplateData.filename());
    }

    @Test
    public void testStorageTemplateDataOnStatefulSingleModel() {
        final List<TemplateData> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", "xoomapp", contents(),
                        StorageType.STATE_STORE, databaseTypes(), ProjectionType.EVENT_BASED, false, false);

        //General Assert

        Assertions.assertEquals(4, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).count());
        Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).count());

        //Assert for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).findFirst().get();

        final TemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("BookState", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("io.vlingo.xoomapp.model.book.BookState"));
        Assertions.assertEquals("BookStateAdapter", stateAdapterTemplateData.filename());

        //Assert for StoreProvider

        final TemplateData storeProviderTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).findFirst().get();

        final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals(Model.DOMAIN, storeProviderParameters.find(TemplateParameter.MODEL));
        Assertions.assertEquals("StateStoreProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
        Assertions.assertEquals(2, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorState"));
        Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.book.BookState"));
        Assertions.assertEquals("BookState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
        Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
        Assertions.assertEquals("AuthorState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
        Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
        Assertions.assertEquals("StateStoreProvider", storeProviderTemplateData.filename());
    }

    @Test
    public void testStorageTemplateDataOnStatefulCQRSModel() {
        final List<TemplateData> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", "xoomapp", contents(),
                        StorageType.STATE_STORE, databaseTypesForCQRS(), ProjectionType.NONE, false, true);

        //General Assert

        Assertions.assertEquals(9, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).count());
        Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER)).count());

        //Assert for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.stream().filter(templateData -> templateData.hasStandard(JavaTemplateStandard.ADAPTER)).findFirst().get();

        final TemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
        Assertions.assertEquals("BookState", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
        Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("io.vlingo.xoomapp.model.book.BookState"));
        Assertions.assertEquals("BookStateAdapter", stateAdapterTemplateData.filename());

        //Assert for StoreProvider

        final List<TemplateData> storeProviders =
                allTemplatesData.stream()
                        .filter(templateData -> templateData.hasStandard(JavaTemplateStandard.STORE_PROVIDER))
                        .collect(Collectors.toList());

        IntStream.range(0, 1).forEach(modelClassificationIndex -> {
            final TemplateData storeProviderTemplateData = storeProviders.get(modelClassificationIndex);
            final Model model = modelClassificationIndex == 0 ? Model.COMMAND : Model.QUERY;
            final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();
            final int expectedImports = modelClassificationIndex == 0 ? 2 : 1;
            Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
            Assertions.assertEquals(model, storeProviderParameters.find(TemplateParameter.MODEL));
            Assertions.assertEquals(model.title + "StateStoreProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
            Assertions.assertEquals(expectedImports, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
            Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.author.AuthorState"));
            Assertions.assertTrue(storeProviderParameters.hasImport("io.vlingo.xoomapp.model.book.BookState"));
            Assertions.assertEquals("BookState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
            Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
            Assertions.assertEquals("AuthorState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
            Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
            Assertions.assertEquals(model.title  + "StateStoreProvider", storeProviderTemplateData.filename());
        });
    }

    private List<Content> contents() {
        return Arrays.asList(
                    Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.java"), null, null, BOOK_STATE_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookRented.java"), null, null, BOOK_RENTED_TEXT),
                    Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookPurchased.java"), null, null, BOOK_PURCHASED_TEXT),
                    Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), null, null, BOOK_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.java"), null, null, AUTHOR_ENTITY_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookEntity.java"), null, null, BOOK_ENTITY_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java"), null, null, PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
                    Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.java"), null, null, BOOK_DATA_CONTENT_TEXT)
                );
    }

    private static Map<Model, DatabaseType> databaseTypesForCQRS() {
        return new HashMap<Model, DatabaseType>() {{
           put(Model.COMMAND, DatabaseType.HSQLDB);
           put(Model.QUERY, DatabaseType.IN_MEMORY);
        }};
    }

    private static Map<Model, DatabaseType> databaseTypes() {
        return new HashMap<Model, DatabaseType>() {{
            put(Model.DOMAIN, DatabaseType.POSTGRES);
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

    private static final String PERSISTENCE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure", "persistence").toString();

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

    private static final String AUTHOR_ENTITY_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public interface AuthorEntity { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_ENTITY_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public interface BookEntity { \\n" +
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

    private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class ProjectionDispatcherProvider { \\n" +
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
