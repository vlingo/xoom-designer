// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.DatabaseType;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class StorageGenerationStepTest {

    private static final String HOME_DIRECTORY = Terminal.supported().isWindows() ? "D:\\projects" : "/home";
    private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();
    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    private static final String PERSISTENCE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure", "persistence").toString();

    @Test
    public void testStorageGenerationWithoutProjections() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        loadProperties(context, DatabaseType.HSQLDB, ProjectionType.NONE);
        loadContents(context);

        new StorageGenerationStep().process(context);

        Assertions.assertEquals(10, context.contents().size());
        Assertions.assertEquals("AuthorStateAdapter.java", context.contents().get(5).file.getName());
        Assertions.assertEquals("BookStateAdapter.java", context.contents().get(6).file.getName());
        Assertions.assertEquals("CommandModelStateStoreProvider.java", context.contents().get(7).file.getName());
        Assertions.assertEquals("QueryModelStateStoreProvider.java", context.contents().get(8).file.getName());
        Assertions.assertEquals("StoreProviderConfiguration.java", context.contents().get(9).file.getName());

        Assertions.assertTrue(context.contents().get(5).text.contains("class AuthorStateAdapter implements StateAdapter<AuthorState,TextState>"));
        Assertions.assertTrue(context.contents().get(6).text.contains("class BookStateAdapter implements StateAdapter<BookState,TextState>"));
        Assertions.assertTrue(context.contents().get(7).text.contains("class CommandModelStateStoreProvider"));
        Assertions.assertTrue(context.contents().get(7).text.contains("import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor.JDBCStateStoreInstantiator;"));
        Assertions.assertFalse(context.contents().get(7).text.contains("InMemoryStateStoreActor"));
        Assertions.assertTrue(context.contents().get(8).text.contains("class QueryModelStateStoreProvider"));
        Assertions.assertTrue(context.contents().get(8).text.contains("HSQLDBConfigurationProvider"));
        Assertions.assertTrue(context.contents().get(8).text.contains("jdbc:hsqldb:mem:"));
        Assertions.assertTrue(context.contents().get(8).text.contains("import io.vlingo.symbio.store.common.jdbc.hsqldb.HSQLDBConfigurationProvider"));
        Assertions.assertTrue(context.contents().get(9).text.contains("class StoreProviderConfiguration"));
        Assertions.assertFalse(context.contents().get(9).text.contains("ProjectionDispatcherProvider"));
    }

    @Test
    public void testStorageGenerationWithProjections() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        loadProperties(context, DatabaseType.IN_MEMORY, ProjectionType.EVENT_BASED);
        loadContents(context);

        new StorageGenerationStep().process(context);

        Assertions.assertEquals(10, context.contents().size());
        Assertions.assertEquals("AuthorStateAdapter.java", context.contents().get(5).file.getName());
        Assertions.assertEquals("BookStateAdapter.java", context.contents().get(6).file.getName());
        Assertions.assertEquals("CommandModelStateStoreProvider.java", context.contents().get(7).file.getName());
        Assertions.assertEquals("QueryModelStateStoreProvider.java", context.contents().get(8).file.getName());
        Assertions.assertEquals("StoreProviderConfiguration.java", context.contents().get(9).file.getName());

        Assertions.assertTrue(context.contents().get(5).text.contains("class AuthorStateAdapter implements StateAdapter<AuthorState,TextState>"));
        Assertions.assertTrue(context.contents().get(6).text.contains("class BookStateAdapter implements StateAdapter<BookState,TextState>"));
        Assertions.assertTrue(context.contents().get(7).text.contains("class CommandModelStateStoreProvider"));
        Assertions.assertTrue(context.contents().get(8).text.contains("class QueryModelStateStoreProvider"));
        Assertions.assertTrue(context.contents().get(9).text.contains("class StoreProviderConfiguration"));
        Assertions.assertTrue(context.contents().get(9).text.contains("import io.vlingo.xoomapp.infrastructure.persistence.ProjectionDispatcherProvider;"));
        Assertions.assertTrue(context.contents().get(9).text.contains("CommandModelStateStoreProvider.using(world.stage(), registry, ProjectionDispatcherProvider.using(world.stage()).storeDispatcher);"));
    }

    private void loadProperties(final TaskExecutionContext context,
                                final DatabaseType databaseType,
                                final ProjectionType projectionType) {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "xoomapp");
        properties.put(CQRS.literal(), "true");
        properties.put(DATABASE.literal(), databaseType.name());
        properties.put(STORAGE_TYPE.literal(), "STATE_STORE");
        properties.put(PROJECTIONS.literal(), projectionType.name());
        properties.put(TARGET_FOLDER.literal(), HOME_DIRECTORY);
        context.onProperties(properties);
    }

    private void loadContents(final TaskExecutionContext context) {
        context.addContent(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT);
        context.addContent(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "Author.java").toString()), AUTHOR_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "Book.java").toString()), BOOK_CONTENT_TEXT);
        context.addContent(PROJECTION_DISPATCHER_PROVIDER, new File(Paths.get(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java").toString()), PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT);
    }

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
