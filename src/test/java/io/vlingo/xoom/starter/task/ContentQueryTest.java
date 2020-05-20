// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.AGGREGATE;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;

public class ContentQueryTest {

    private static final String AUTHOR_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
            "import io.vlingo.symbio.store.object.StateObject; \\n" +
            "public class AuthorState { \\n" +
            "... \\n" +
            "}";

    private static final String BOOK_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "import io.vlingo.symbio.store.object.StateObject; \\n" +
                    "public class BookState { \\n" +
                    "... \\n" +
                    "}";

    private static final String AGGREGATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
            "public class Author { \\n" +
            "... \\n" +
             "}";

    @Test
    public void testClassNameQuery() {
        final List<String> classNames =
                ContentQuery.findClassNames(STATE, contents());

        Assertions.assertEquals(2, classNames.size());
        Assertions.assertTrue(classNames.contains("AuthorState"));
        Assertions.assertTrue(classNames.contains("BookState"));
    }

    @Test
    public void testQualifiedClassNameQuery() {
        final List<String> classNames =
                ContentQuery.findFullyQualifiedClassNames(STATE, contents());

        Assertions.assertEquals(2, classNames.size());
        Assertions.assertTrue(classNames.contains("io.vlingo.xoomapp.model.AuthorState"));
        Assertions.assertTrue(classNames.contains("io.vlingo.xoomapp.model.BookState"));
    }

    private List<Content> contents() {
        return Arrays.asList(
            Content.with(STATE, new File(Paths.get("/Projects/", "AuthorState.java").toString()), AUTHOR_STATE_CONTENT_TEXT),
            Content.with(STATE, new File(Paths.get("/Projects/", "BookState.java").toString()), BOOK_STATE_CONTENT_TEXT),
            Content.with(AGGREGATE, new File(Paths.get("/Projects/", "Author.java").toString()), AGGREGATE_CONTENT_TEXT)
        );
    }
}
