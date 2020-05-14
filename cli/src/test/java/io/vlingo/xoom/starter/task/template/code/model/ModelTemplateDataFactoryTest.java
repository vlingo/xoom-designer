package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.StorageType.JOURNAL;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class ModelTemplateDataFactoryTest {

    private static final String PROJECT_PATH =
            Terminal.supported().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java", "io", "vlingo", "xoomapp", "model").toString();

    @Test
    public void testAggregateTemplateDataBuild() {
        final String basePackage = "io.vlingo.xoomapp";
        final String aggregatesData =
                "Author;AuthorAccepted;AuthorRegistered|Book;BookPurchased;BookPublished";

        final List<ModelTemplateData> templateData =
                ModelTemplateDataFactory.build(basePackage, PROJECT_PATH, aggregatesData, JOURNAL);

        final String authorPackagePath = Paths.get(MODEL_PACKAGE_PATH, "author").toString();
        final String bookPackagePath = Paths.get(MODEL_PACKAGE_PATH, "book").toString();

        final ModelTemplateData author = templateData.get(0);
        final ModelTemplateData book = templateData.get(1);

        Assertions.assertEquals(2, templateData.size());
        Assertions.assertEquals(2, author.events.size());

        Assertions.assertEquals("Author", author.name);
        Assertions.assertEquals(JOURNAL, author.storageType);
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", author.packageName);
        Assertions.assertEquals(Paths.get(authorPackagePath, "Author.java").toString(), author.files.get(AGGREGATE_PROTOCOL).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorEntity.java").toString(), author.files.get(AGGREGATE).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorState.java").toString(), author.files.get(STATE).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorPlaceholderDefined.java").toString(), author.files.get(PLACEHOLDER_DOMAIN_EVENT).get(0).getAbsolutePath());

        Assertions.assertEquals("AuthorAccepted", author.events.get(0).name);
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", author.events.get(0).packageName);
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorAccepted.java").toString(), author.events.get(0).file().getAbsolutePath());

        Assertions.assertEquals("AuthorRegistered", author.events.get(1).name);
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", author.events.get(1).packageName);
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorRegistered.java").toString(), author.events.get(1).file().getAbsolutePath());

        Assertions.assertEquals("Book", book.name);
        Assertions.assertEquals(JOURNAL, book.storageType);
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", book.packageName);
        Assertions.assertEquals(Paths.get(bookPackagePath, "Book.java").toString(), book.files.get(AGGREGATE_PROTOCOL).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookEntity.java").toString(), book.files.get(AGGREGATE).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookState.java").toString(), book.files.get(STATE).get(0).getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPlaceholderDefined.java").toString(), book.files.get(PLACEHOLDER_DOMAIN_EVENT).get(0).getAbsolutePath());

        Assertions.assertEquals("BookPurchased", book.events.get(0).name);
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", book.events.get(0).packageName);
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPurchased.java").toString(), book.events.get(0).file().getAbsolutePath());
        Assertions.assertEquals("BookPublished", book.events.get(1).name);
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", book.events.get(1).packageName);
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPublished.java").toString(), book.events.get(1).file().getAbsolutePath());
    }

}
