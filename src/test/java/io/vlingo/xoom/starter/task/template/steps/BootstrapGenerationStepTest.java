// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class BootstrapGenerationStepTest {

    @Test
    public void testBootstrapGeneration() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        loadProperties(context);
        loadContents(context);

        new BootstrapGenerationStep().process(context);

        Assertions.assertEquals(6, context.contents().size());
        Assertions.assertEquals("Bootstrap.java", context.contents().get(5).file.getName());
        Assertions.assertTrue(context.contents().get(5).text.contains("final ProjectionDispatcherProvider projectionDispatcherProvider"));
        Assertions.assertTrue(context.contents().get(5).text.contains("CommandModelStateStoreProvider.using(stage, registry, projectionDispatcherProvider.storeDispatcher)"));
        Assertions.assertTrue(context.contents().get(5).text.contains("QueryModelStateStoreProvider.using(stage, registry)"));
        Assertions.assertTrue(context.contents().get(5).text.contains("final AuthorResource authorResource = new AuthorResource();"));
        Assertions.assertTrue(context.contents().get(5).text.contains("final BookResource bookResource = new BookResource();"));
        Assertions.assertTrue(context.contents().get(5).text.contains("authorResource.routes(),"));
        Assertions.assertTrue(context.contents().get(5).text.contains("bookResource.routes()"));
        Assertions.assertFalse(context.contents().get(5).text.contains("bookResource.routes(),"));
    }

    private void loadProperties(final TaskExecutionContext context) {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "xoomapp");
        properties.put(CQRS.literal(), "true");
        properties.put(TARGET_FOLDER.literal(), HOME_DIRECTORY);
        properties.put(STORAGE_TYPE.literal(), "STATE_STORE");
        properties.put(PROJECTIONS.literal(), ProjectionType.OPERATION_BASED.name());
        context.onProperties(properties);
    }

    private void loadContents(final TaskExecutionContext context) {
        context.addContent(REST_RESOURCE, new File(Paths.get(RESOURCE_PACKAGE_PATH, "AuthorResource.java").toString()), AUTHOR_RESOURCE_CONTENT);
        context.addContent(REST_RESOURCE, new File(Paths.get(RESOURCE_PACKAGE_PATH, "BookResource.java").toString()), BOOK_RESOURCE_CONTENT);
        context.addContent(STORE_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "CommandModelStateStoreProvider.java").toString()), COMMAND_MODEL_STORE_PROVIDER_CONTENT);
        context.addContent(STORE_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "QueryModelStateStoreProvider.java").toString()), QUERY_MODEL_STORE_PROVIDER_CONTENT);
        context.addContent(PROJECTION_DISPATCHER_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java").toString()), PROJECTION_DISPATCHER_PROVIDER_CONTENT);
    }

    private static final String HOME_DIRECTORY = Terminal.supported().isWindows() ? "D:\\projects" : "/home";

    private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();

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

    private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT =
            "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                    "public class ProjectionDispatcherProvider { \\n" +
                    "... \\n" +
                    "}";
}
