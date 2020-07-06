// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class BootstrapTemplateDataTest {

    @Test
    public void testBootstrapTemplateDataGenerationWithCQRSAndProjections() {
        final List<Content> contents =
                Arrays.asList(
                        Content.with(REST_RESOURCE, new File(Paths.get(RESOURCE_PACKAGE_PATH, "AuthorResource.java").toString()), AUTHOR_RESOURCE_CONTENT),
                        Content.with(REST_RESOURCE, new File(Paths.get(RESOURCE_PACKAGE_PATH, "BookResource.java").toString()), BOOK_RESOURCE_CONTENT),
                        Content.with(STORE_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "CommandModelStateStoreProvider.java").toString()), COMMAND_MODEL_STORE_PROVIDER_CONTENT),
                        Content.with(STORE_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "QueryModelStateStoreProvider.java").toString()), QUERY_MODEL_STORE_PROVIDER_CONTENT),
                        Content.with(PROJECTION_DISPATCHER_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java").toString()), PROJECTION_DISPATCHER_PROVIDER_CONTENT)
                );

        final TemplateData bootstrapTemplateData =
                BootstrapTemplateData.from("io.vlingo.xoomapp", PROJECT_PATH, "xoom-app",
                        StorageType.STATE_STORE, true, true, contents);

        final CodeTemplateParameters parameters =
                bootstrapTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, parameters.find(PACKAGE_NAME));
        Assertions.assertEquals(7, parameters.<List>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.resource.AuthorResource", parameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.resource.BookResource", parameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.persistence.CommandModelStateStoreProvider", parameters.<List<ImportParameter>>find(IMPORTS).get(2).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.persistence.QueryModelStateStoreProvider", parameters.<List<ImportParameter>>find(IMPORTS).get(3).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.persistence.ProjectionDispatcherProvider", parameters.<List<ImportParameter>>find(IMPORTS).get(4).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoom.annotation.initializer.ResourceHandlers", parameters.<List<ImportParameter>>find(IMPORTS).get(5).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", parameters.<List<ImportParameter>>find(IMPORTS).get(6).getQualifiedClassName());

        Assertions.assertEquals(true, parameters.<RestResourceParameter>find(CodeTemplateParameter.REST_RESOURCE).getExist());
        Assertions.assertEquals("AuthorResource.class, BookResource.class", parameters.<RestResourceParameter>find(CodeTemplateParameter.REST_RESOURCE).getClassNames());

        Assertions.assertEquals(3, parameters.<List>find(PROVIDERS).size());
        Assertions.assertEquals("QueryModelStateStoreProvider", parameters.<List<ProviderParameter>>find(PROVIDERS).get(0).getInitialization());
        Assertions.assertEquals("stage, statefulTypeRegistry", parameters.<List<ProviderParameter>>find(PROVIDERS).get(0).getArguments());
        Assertions.assertEquals("final ProjectionDispatcherProvider projectionDispatcherProvider = ProjectionDispatcherProvider", parameters.<List<ProviderParameter>>find(PROVIDERS).get(1).getInitialization());
        Assertions.assertEquals("stage", parameters.<List<ProviderParameter>>find(PROVIDERS).get(1).getArguments());
        Assertions.assertEquals("CommandModelStateStoreProvider", parameters.<List<ProviderParameter>>find(PROVIDERS).get(2).getInitialization());
        Assertions.assertEquals("stage, statefulTypeRegistry, projectionDispatcherProvider.storeDispatcher", parameters.<List<ProviderParameter>>find(PROVIDERS).get(2).getArguments());

        Assertions.assertEquals(1, parameters.<List>find(TYPE_REGISTRIES).size());
        Assertions.assertEquals("StatefulTypeRegistry", parameters.<List<TypeRegistryParameter>>find(TYPE_REGISTRIES).get(0).getClassName());
        Assertions.assertEquals("statefulTypeRegistry", parameters.<List<TypeRegistryParameter>>find(TYPE_REGISTRIES).get(0).getObjectName());

        Assertions.assertEquals(true, parameters.find(USE_PROJECTIONS));
        Assertions.assertEquals("xoom-app", parameters.find(APPLICATION_NAME));
    }

    @Test
    public void testBootstrapTemplateDataGenerationWithoutCQRSAndProjections() {
        final List<Content> contents =
                Arrays.asList(
                        Content.with(REST_RESOURCE, new File(Paths.get(RESOURCE_PACKAGE_PATH, "AuthorResource.java").toString()), AUTHOR_RESOURCE_CONTENT),
                        Content.with(STORE_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "StateStoreProvider.java").toString()), SINGLE_MODEL_STORE_PROVIDER_CONTENT)
                );

        final TemplateData bootstrapTemplateData =
                BootstrapTemplateData.from("io.vlingo.xoomapp", PROJECT_PATH, "xoom-app",
                        StorageType.STATE_STORE, false, false, contents);

        final CodeTemplateParameters parameters =
                bootstrapTemplateData.parameters();

        Assertions.assertEquals(EXPECTED_PACKAGE, parameters.find(PACKAGE_NAME));
        Assertions.assertEquals(4, parameters.<List>find(IMPORTS).size());
        Assertions.assertEquals("io.vlingo.xoomapp.resource.AuthorResource", parameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.persistence.StateStoreProvider", parameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.xoom.annotation.initializer.ResourceHandlers", parameters.<List<ImportParameter>>find(IMPORTS).get(2).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.lattice.model.stateful.StatefulTypeRegistry", parameters.<List<ImportParameter>>find(IMPORTS).get(3).getQualifiedClassName());

        Assertions.assertEquals(true, parameters.<RestResourceParameter>find(CodeTemplateParameter.REST_RESOURCE).getExist());
        Assertions.assertEquals("AuthorResource.class", parameters.<RestResourceParameter>find(CodeTemplateParameter.REST_RESOURCE).getClassNames());

        Assertions.assertEquals(1, parameters.<List>find(PROVIDERS).size());
        Assertions.assertEquals("StateStoreProvider", parameters.<List<ProviderParameter>>find(PROVIDERS).get(0).getInitialization());
        Assertions.assertEquals("stage, statefulTypeRegistry", parameters.<List<ProviderParameter>>find(PROVIDERS).get(0).getArguments());

        Assertions.assertEquals(1, parameters.<List>find(TYPE_REGISTRIES).size());
        Assertions.assertEquals("StatefulTypeRegistry", parameters.<List<TypeRegistryParameter>>find(TYPE_REGISTRIES).get(0).getClassName());
        Assertions.assertEquals("statefulTypeRegistry", parameters.<List<TypeRegistryParameter>>find(TYPE_REGISTRIES).get(0).getObjectName());

        Assertions.assertEquals(false, parameters.find(USE_PROJECTIONS));
        Assertions.assertEquals("xoom-app", parameters.find(APPLICATION_NAME));
    }

    private static final String EXPECTED_PACKAGE = "io.vlingo.xoomapp.infrastructure";

    private static final String PROJECT_PATH =
            Terminal.supported().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String RESOURCE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "resource").toString();

    private static final String INFRASTRUCTURE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure").toString();

    private static final String PERSISTENCE_PACKAGE_PATH =
            Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

    private static final String AUTHOR_RESOURCE_CONTENT =
            "package io.vlingo.xoomapp.resource; \\n" +
                    "public class AuthorResource { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_RESOURCE_CONTENT =
            "package io.vlingo.xoomapp.resource; \\n" +
                    "public class BookResource { \\n" +
                    "... \\n" +
                    "}";

    private static final String COMMAND_MODEL_STORE_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class CommandModelStateStoreProvider { \\n" +
                    "... \\n" +
                    "}";

    private static final String QUERY_MODEL_STORE_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class QueryModelStateStoreProvider { \\n" +
                    "... \\n" +
                    "}";

    private static final String SINGLE_MODEL_STORE_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class StateStoreProvider { \\n" +
                    "... \\n" +
                    "}";

    private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class ProjectionDispatcherProvider { \\n" +
                    "... \\n" +
                    "}";
}
