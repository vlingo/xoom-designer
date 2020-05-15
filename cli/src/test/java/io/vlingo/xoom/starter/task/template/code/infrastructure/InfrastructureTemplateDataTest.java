// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
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
import static io.vlingo.xoom.starter.task.template.code.infrastructure.ModelClassification.*;

public class InfrastructureTemplateDataTest {

    @Test
    public void testInfraTemplateDataOnStatefulSingleModel() {
        final InfrastructureTemplateData templateData =
                InfrastructureTemplateData.with("io.vlingo.xoomapp", PROJECT_PATH, false,
                        StorageType.STATE_STORE, DatabaseType.IN_MEMORY, contents());

        //General Assertions

        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData = templateData.collectAll();

        Assertions.assertEquals(3, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(STATE_ADAPTER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).size());

        //Assertions for StoreProviderConfiguration

        final TemplateData storeProviderConfigurationTemplateData =
                allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).get(0);

        final CodeTemplateParameters storeProvideConfigurationParameters =
                storeProviderConfigurationTemplateData.templateParameters();

        Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, "StoreProviderConfiguration.java").toString(),
                storeProviderConfigurationTemplateData.file().getAbsolutePath());
        Assertions.assertEquals(EXPECTED_PACKAGE, storeProvideConfigurationParameters.from(PACKAGE_NAME));
        Assertions.assertEquals("StatefulTypeRegistry", storeProvideConfigurationParameters.from(REGISTRY_CLASS_NAME));
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", storeProvideConfigurationParameters.from(REGISTRY_QUALIFIED_CLASS_NAME));
        Assertions.assertEquals(1, storeProvideConfigurationParameters.<List>from(STORE_PROVIDERS).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(STATE_ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.from(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.from(STATE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.from(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>from(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>from(IMPORTS).get(0).qualifiedClassName);
        Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, "AuthorStateAdapter.java").toString(), stateAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        final TemplateData storeProviderTemplateData =
                allTemplatesData.get(STORE_PROVIDER).get(0);

        final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.from(PACKAGE_NAME));
        Assertions.assertEquals(SINGLE, storeProviderParameters.from(MODEL_CLASSIFICATION));
        Assertions.assertEquals("StateStoreProvider", storeProviderParameters.from(STORE_PROVIDER_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor", storeProviderParameters.from(STORE_CLASS_NAME));
        Assertions.assertEquals(2, storeProviderParameters.<List<ImportParameter>>from(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", storeProviderParameters.<List<ImportParameter>>from(IMPORTS).get(0).qualifiedClassName);
        Assertions.assertEquals("io.vlingo.xoomapp.model.BookState", storeProviderParameters.<List<ImportParameter>>from(IMPORTS).get(1).qualifiedClassName);
        Assertions.assertEquals(2, storeProviderParameters.<List<StateAdapterParameter>>from(IMPORTS).size());
        Assertions.assertEquals("AuthorState", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(0).stateClass);
        Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(0).stateAdapterClass);
        Assertions.assertEquals("BookState", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(1).stateClass);
        Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(1).stateAdapterClass);
        Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
    }

    @Test
    public void testInfraTemplateDataOnStatefulCQRSModel() {
        final InfrastructureTemplateData templateData =
                InfrastructureTemplateData.with("io.vlingo.xoomapp", PROJECT_PATH, true,
                        StorageType.STATE_STORE, DatabaseType.IN_MEMORY, contents());

        //General Assertions

        final Map<CodeTemplateStandard, List<TemplateData>> allTemplatesData = templateData.collectAll();

        Assertions.assertEquals(3, allTemplatesData.size());
        Assertions.assertEquals(2, allTemplatesData.get(STATE_ADAPTER).size());
        Assertions.assertEquals(2, allTemplatesData.get(STORE_PROVIDER).size());
        Assertions.assertEquals(1, allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).size());

        //Assertions for StoreProviderConfiguration

        final TemplateData storeProviderConfigurationTemplateData =
                allTemplatesData.get(STORE_PROVIDER_CONFIGURATION).get(0);

        final CodeTemplateParameters storeProvideConfigurationParameters =
                storeProviderConfigurationTemplateData.templateParameters();

        Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, "StoreProviderConfiguration.java").toString(),
                storeProviderConfigurationTemplateData.file().getAbsolutePath());
        Assertions.assertEquals(EXPECTED_PACKAGE, storeProvideConfigurationParameters.from(PACKAGE_NAME));
        Assertions.assertEquals("StatefulTypeRegistry", storeProvideConfigurationParameters.from(REGISTRY_CLASS_NAME));
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", storeProvideConfigurationParameters.from(REGISTRY_QUALIFIED_CLASS_NAME));
        Assertions.assertEquals(2, storeProvideConfigurationParameters.<List>from(STORE_PROVIDERS).size());

        //Assertions for StateAdapter

        final TemplateData stateAdapterTemplateData =
                allTemplatesData.get(STATE_ADAPTER).get(0);

        final CodeTemplateParameters stateAdapterConfigurationParameters =
                stateAdapterTemplateData.templateParameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.from(PACKAGE_NAME));
        Assertions.assertEquals("AuthorState", stateAdapterConfigurationParameters.from(STATE_NAME));
        Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.from(STORAGE_TYPE));
        Assertions.assertEquals(1, stateAdapterConfigurationParameters.<List<ImportParameter>>from(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", stateAdapterConfigurationParameters.<List<ImportParameter>>from(IMPORTS).get(0).qualifiedClassName);
        Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, "AuthorStateAdapter.java").toString(), stateAdapterTemplateData.file().getAbsolutePath());

        //Assertions for StoreProvider

        IntStream.range(0, 1).forEach(modelClassificationIndex -> {
            final TemplateData storeProviderTemplateData = allTemplatesData.get(STORE_PROVIDER).get(modelClassificationIndex);
            final ModelClassification modelClassification = modelClassificationIndex == 0 ? COMMAND : QUERY;
            final CodeTemplateParameters storeProviderParameters = storeProviderTemplateData.templateParameters();
            Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.from(PACKAGE_NAME));
            Assertions.assertEquals(modelClassification, storeProviderParameters.from(MODEL_CLASSIFICATION));
            Assertions.assertEquals(modelClassification.title + "StateStoreProvider", storeProviderParameters.from(STORE_PROVIDER_NAME));
            Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor", storeProviderParameters.from(STORE_CLASS_NAME));
            Assertions.assertEquals(2, storeProviderParameters.<List<ImportParameter>>from(IMPORTS).size());
            Assertions.assertEquals("io.vlingo.xoomapp.model.AuthorState", storeProviderParameters.<List<ImportParameter>>from(IMPORTS).get(0).qualifiedClassName);
            Assertions.assertEquals("io.vlingo.xoomapp.model.BookState", storeProviderParameters.<List<ImportParameter>>from(IMPORTS).get(1).qualifiedClassName);
            Assertions.assertEquals(2, storeProviderParameters.<List<StateAdapterParameter>>from(IMPORTS).size());
            Assertions.assertEquals("AuthorState", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(0).stateClass);
            Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(0).stateAdapterClass);
            Assertions.assertEquals("BookState", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(1).stateClass);
            Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<StateAdapterParameter>>from(STATE_ADAPTERS).get(1).stateAdapterClass);
            Assertions.assertEquals(Paths.get(INFRA_PACKAGE_PATH, modelClassification.title  + "StateStoreProvider.java").toString(), storeProviderTemplateData.file().getAbsolutePath());
        });
    }

    private List<Content> contents() {
        return Arrays.asList(
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT),
                  Content.with(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT),
                  Content.with(AGGREGATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorEntity.java").toString()), AUTHOR_CONTENT_TEXT),
                  Content.with(AGGREGATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookEntity.java").toString()), BOOK_CONTENT_TEXT)
                );
    }

    private static final String PROJECT_PATH =
            Terminal.supported().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    private static final String INFRA_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure").toString();

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
                    "public class Author { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "public class Book { \\n" +
                    "... \\n" +
                    "}";

    private static final String EXPECTED_PACKAGE = "io.vlingo.xoomapp.infrastructure";
}
