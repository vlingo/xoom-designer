// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.AGGREGATE_PROTOCOL;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;

public class ProjectionGenerationStepTest {

    private static final String HOME_DIRECTORY = Terminal.supported().isWindows() ? "D:\\projects" : "/home";
    private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();
    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    @Test
    public void testEventBasedProjectionGeneration() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        loadProperties(context, ProjectionType.EVENT_BASED.name());
        loadContents(context);
        new ProjectionGenerationStep().process(context);
        performAssertion(context);
    }

    @Test
    public void testOperationBasedProjectionGeneration() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        loadProperties(context, ProjectionType.OPERATION_BASED.name());
        loadContents(context);
        new ProjectionGenerationStep().process(context);
        performAssertion(context);
    }

    private void performAssertion(final TaskExecutionContext context) {
        final ProjectionType projectionType = context.propertyOf(PROJECTIONS, ProjectionType::valueOf);
        final String expectedProjectionComment = projectionType.isEventBased() ? "replace with event" : "replace with operation text";

        Assertions.assertEquals(9, context.contents().size());
        Assertions.assertEquals("AuthorData.java", context.contents().get(4).file.getName());
        Assertions.assertEquals("BookData.java", context.contents().get(5).file.getName());
        Assertions.assertEquals("AuthorProjectionActor.java", context.contents().get(6).file.getName());
        Assertions.assertEquals("BookProjectionActor.java", context.contents().get(7).file.getName());
        Assertions.assertEquals("ProjectionDispatcherProvider.java", context.contents().get(8).file.getName());

        Assertions.assertTrue(context.contents().get(4).text.contains("class AuthorData"));
        Assertions.assertTrue(context.contents().get(5).text.contains("class BookData"));
        Assertions.assertTrue(context.contents().get(6).text.contains("class AuthorProjectionActor extends StateStoreProjectionActor<AuthorData>"));
        Assertions.assertTrue(context.contents().get(6).text.contains(expectedProjectionComment));
        Assertions.assertTrue(context.contents().get(7).text.contains("class BookProjectionActor extends StateStoreProjectionActor<BookData>"));
        Assertions.assertTrue(context.contents().get(7).text.contains(expectedProjectionComment));
        Assertions.assertTrue(context.contents().get(8).text.contains("class ProjectionDispatcherProvider"));
    }

    private void loadContents(final TaskExecutionContext context) {
        context.addContent(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT);
        context.addContent(STATE, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "author", "Author.java").toString()), AUTHOR_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new File(Paths.get(MODEL_PACKAGE_PATH, "book", "Book.java").toString()), BOOK_CONTENT_TEXT);
    }

    private void loadProperties(final TaskExecutionContext context, final String projections) {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "xoomapp");
        properties.put(PROJECTIONS.literal(), projections);
        properties.put(TARGET_FOLDER.literal(), HOME_DIRECTORY);
        context.onProperties(properties);
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

}
