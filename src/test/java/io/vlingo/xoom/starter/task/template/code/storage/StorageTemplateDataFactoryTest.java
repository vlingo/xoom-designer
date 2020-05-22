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
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.*;

public class StorageTemplateDataFactoryTest {

    @Test
    public void testStorageTemplateDataOnStatefulSingleModel() {
        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", PROJECT_PATH, false,
                        contents(), StorageType.STATE_STORE, DatabaseType.POSTGRES, ProjectionType.EVENT_BASED);

        //General Assertions

        Assertions.assertEquals(3, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(STATE_ADAPTER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).size());

        //Assertions for StoreProviderConfiguration

        final TemplateData storeProviderConfigurationTemplateData =
                allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).get(0);

        final CodeTemplateParameters storeProvideConfigurationParameters =
                storeProviderConfigurationTemplateData.templateParameters();

        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "StoreProviderConfiguration.java").toString(),
                storeProviderConfigurationTemplateData.file().getAbsolutePath());
        Assertions.assertEquals(EXPECTED_PACKAGE, storeProvideConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("StatefulTypeRegistry", storeProvideConfigurationParameters.find(REGISTRY_CLASS_NAME));
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", storeProvideConfigurationParameters.find(REGISTRY_QUALIFIED_CLASS_NAME));
        Assertions.assertEquals(1, storeProvideConfigurationParameters.<List>find(STORE_PROVIDERS).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(STATE_ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.find(STATE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);
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
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);
        Assertions.assertEquals("io.vlingo.xoomapp.model.BookState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(1).qualifiedClassName);
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(2).qualifiedClassName);
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(3).qualifiedClassName);
        Assertions.assertEquals("PostgresStorageDelegate", storeProviderParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("PostgresConfigurationProvider", storeProviderParameters.find(CONFIGURATION_PROVIDER_NAME));
        Assertions.assertEquals("AuthorState", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(0).stateClass);
        Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(0).stateAdapterClass);
        Assertions.assertEquals("BookState", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(1).stateClass);
        Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(1).stateAdapterClass);
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
    }

    @Test
    public void testStorageTemplateDataOnStatefulCQRSModel() {
        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData =
                StorageTemplateDataFactory.build("io.vlingo.xoomapp", PROJECT_PATH, true,
                        contents(), StorageType.STATE_STORE, DatabaseType.IN_MEMORY, ProjectionType.NONE);

        //General Assertions

        Assertions.assertEquals(3, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(STATE_ADAPTER).size());
        Assertions.assertEquals(2, allTemplatesData.get(STORE_PROVIDER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).size());

        //Assertions for StoreProviderConfiguration

        final TemplateData storeProviderConfigurationTemplateData =
                allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).get(0);

        final CodeTemplateParameters storeProvideConfigurationParameters =
                storeProviderConfigurationTemplateData.templateParameters();

        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "StoreProviderConfiguration.java").toString(),
                storeProviderConfigurationTemplateData.file().getAbsolutePath());
        Assertions.assertEquals(EXPECTED_PACKAGE, storeProvideConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("StatefulTypeRegistry", storeProvideConfigurationParameters.find(REGISTRY_CLASS_NAME));
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", storeProvideConfigurationParameters.find(REGISTRY_QUALIFIED_CLASS_NAME));
        Assertions.assertEquals(2, storeProvideConfigurationParameters.<List>find(STORE_PROVIDERS).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(STATE_ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.find(STATE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);
        Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, "AuthorStateAdapter.java").toString(), stateAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        IntStream.range(0, 1).forEach(modelClassificationIndex -> {
            final TemplateData storeProviderTemplateData = allTemplatesData.get(STORE_PROVIDER).get(modelClassificationIndex);
            final ModelClassification modelClassification = modelClassificationIndex == 0 ? COMMAND : QUERY;
            final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();
            Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(PACKAGE_NAME));
            Assertions.assertEquals(modelClassification, storeProviderParameters.find(MODEL_CLASSIFICATION));
            Assertions.assertEquals(modelClassification.title + "StateStoreProvider", storeProviderParameters.find(STORE_PROVIDER_NAME));
            Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor", storeProviderParameters.find(STORE_NAME));
            Assertions.assertEquals(2, storeProviderParameters.<List<ImportParameter>>find(IMPORTS).size());
            Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);
            Assertions.assertEquals("io.vlingo.xoomapp.model.BookState", storeProviderParameters.<List<ImportParameter>>find(IMPORTS).get(1).qualifiedClassName);
            Assertions.assertEquals("AuthorState", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(0).stateClass);
            Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(0).stateAdapterClass);
            Assertions.assertEquals("BookState", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(1).stateClass);
            Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>find(STATE_ADAPTERS).get(1).stateAdapterClass);
            Assertions.assertEquals(Paths.get(PERSISTENCE_PACKAGE_PATH, modelClassification.title  + "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
        });
    }

    private List<Content> contents() {
        return Arrays.asList(
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT),
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT),
                  Content.with(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "Author.java").toString()), AUTHOR_CONTENT_TEXT),
                  Content.with(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "Book.java").toString()), BOOK_CONTENT_TEXT),
                  Content.with(PROJECTION_DISPATCHER_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java").toString()), PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT)
                );
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
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class AuthorState { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class BookState { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public interface Author { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public interface Book { \\n" +
                    "... \\n" +
                    "}";

    private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class ProjectionDispatcherProvider { \\n" +
                    "... \\n" +
                    "}";
}
