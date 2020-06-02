// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.*;

public class StorageTemplateDataFactoryTest {

    @Test
    public void testStorageTemplateDataOnSourcedSingleModel() {
        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", PROJECT_PATH, false,
                        contents(), StorageType.JOURNAL, databaseTypes(), ProjectionType.EVENT_BASED);

        //General Assertions

        Assertions.assertEquals(2, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(ADAPTER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER).size());

        //Assertions for StateAdapter

        final TemplateData entryAdapterTemplateData =
                allTemplatesData.get(ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                entryAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("BookRented", stateAdapterConfigurationParameters.find(SOURCE_NAME));
        Assertions.assertEquals(StorageType.JOURNAL, stateAdapterConfigurationParameters.find(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.book.BookRented", stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "BookRentedAdapter.java").toString(), entryAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        final TemplateData storeProviderTemplateData =
                allTemplatesData.get(STORE_PROVIDER).get(0);

        final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(PACKAGE_NAME));
        Assertions.assertEquals(SINGLE, storeProviderParameters.find(MODEL_CLASSIFICATION));
        Assertions.assertEquals("JournalProvider", storeProviderParameters.find(STORE_PROVIDER_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor", storeProviderParameters.find(STORE_NAME));
        Assertions.assertEquals(3, storeProviderParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.book.BookRented", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.model.book.BookPurchased", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(2).getQualifiedClassName());
        Assertions.assertEquals("PostgresConfigurationProvider", storeProviderParameters.find(CONFIGURATION_PROVIDER_NAME));
        Assertions.assertEquals("BookRented", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getSourceClass());
        Assertions.assertEquals("BookRentedAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getAdapterClass());
        Assertions.assertEquals("BookPurchased", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getSourceClass());
        Assertions.assertEquals("BookPurchasedAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getAdapterClass());
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "JournalProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
    }

    @Test
    public void testStorageTemplateDataOnStatefulSingleModel() {
        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", PROJECT_PATH, false,
                        contents(), StorageType.STATE_STORE, databaseTypes(), ProjectionType.EVENT_BASED);

        //General Assertions

        Assertions.assertEquals(2, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(ADAPTER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.find(SOURCE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.author.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "AuthorStateAdapter.java").toString(), stateAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        final TemplateData storeProviderTemplateData =
                allTemplatesData.get(STORE_PROVIDER).get(0);

        final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(PACKAGE_NAME));
        Assertions.assertEquals(SINGLE, storeProviderParameters.find(MODEL_CLASSIFICATION));
        Assertions.assertEquals("StateStoreProvider", storeProviderParameters.find(STORE_PROVIDER_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor", storeProviderParameters.find(STORE_NAME));
        Assertions.assertEquals(4, storeProviderParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.author.AuthorState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.model.book.BookState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(2).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(3).getQualifiedClassName());
        Assertions.assertEquals("PostgresStorageDelegate", storeProviderParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("PostgresConfigurationProvider", storeProviderParameters.find(CONFIGURATION_PROVIDER_NAME));
        Assertions.assertEquals("AuthorState", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getSourceClass());
        Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getAdapterClass());
        Assertions.assertEquals("BookState", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getSourceClass());
        Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getAdapterClass());
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
    }

    @Test
    public void testStorageTemplateDataOnStatefulCQRSModel() {
        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", PROJECT_PATH, true,
                        contents(), StorageType.STATE_STORE, databaseTypes(), ProjectionType.NONE);

        //General Assertions

        Assertions.assertEquals(2, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(ADAPTER).size());
        Assertions.assertEquals(2, allTemplatesData.get(STORE_PROVIDER).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.find(SOURCE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.author.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "AuthorStateAdapter.java").toString(), stateAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        IntStream.range(0, 1).forEach(modelClassificationIndex -> {
            final TemplateData storeProviderTemplateData = allTemplatesData.get(STORE_PROVIDER).get(modelClassificationIndex);
            final ModelClassification modelClassification = modelClassificationIndex == 0 ? COMMAND : QUERY;
            final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();
            final String expectedStateStoreActor = modelClassificationIndex == 0 ? "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor" : "io.vlingo.symbio.store.state.jdbc.InMemoryStateStoreActor";
            final int expectedImports = modelClassificationIndex == 0 ? 4 : 2;
            Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(PACKAGE_NAME));
            Assertions.assertEquals(modelClassification, storeProviderParameters.find(MODEL_CLASSIFICATION));
            Assertions.assertEquals(modelClassification.title + "StateStoreProvider", storeProviderParameters.find(STORE_PROVIDER_NAME));
            Assertions.assertEquals(expectedStateStoreActor, storeProviderParameters.find(STORE_NAME));
            Assertions.assertEquals(expectedImports, storeProviderParameters.<List<ImportParameter>>find(IMPORTS).size());
            Assertions.assertEquals("io.vlingo.xoomapp.model.author.AuthorState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
            Assertions.assertEquals("io.vlingo.xoomapp.model.book.BookState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
            Assertions.assertEquals("AuthorState", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getSourceClass());
            Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(0).getAdapterClass());
            Assertions.assertEquals("BookState", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getSourceClass());
            Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<AdapterParameter>>find(ADAPTERS).get(1).getAdapterClass());
            Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, modelClassification.title  + "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
        });
    }

    private List<Content> contents() {
        return Arrays.asList(
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT),
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT),
                  Content.with(DOMAIN_EVENT, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookRented.java").toString()), BOOK_RENTED_TEXT),
                  Content.with(DOMAIN_EVENT, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookPurchased.java").toString()), BOOK_PURCHASED_TEXT),
                  Content.with(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "Author.java").toString()), AUTHOR_CONTENT_TEXT),
                  Content.with(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "Book.java").toString()), BOOK_CONTENT_TEXT),
                  Content.with(PROJECTION_DISPATCHER_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java").toString()), PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT)
                );
    }

    private static final Map<ModelClassification, DatabaseType> databaseTypes() {
        return new HashMap<ModelClassification, DatabaseType>() {{
           put(SINGLE, DatabaseType.POSTGRES);
           put(COMMAND, DatabaseType.HSQLDB);
           put(QUERY, DatabaseType.IN_MEMORY);
        }};
    }

    private static final String EXPECTED_PACKAGE = "io.vlingo.xoomapp.infrastructure.persistence";

    private static final String PROJECT_PATH =
            Terminal.supported().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    private static final String PERSISTENCE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure", "persistence").toString();

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

    private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class ProjectionDispatcherProvider { \\n" +
                    "... \\n" +
                    "}";
}
