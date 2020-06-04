package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.storage.StorageType.JOURNAL;
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

        final List<TemplateData> templatesData =
                ModelTemplateDataFactory.build(basePackage, PROJECT_PATH, aggregatesData, JOURNAL);

        final String authorPackagePath = Paths.get(MODEL_PACKAGE_PATH, "author").toString();
        final String bookPackagePath = Paths.get(MODEL_PACKAGE_PATH, "book").toString();

        Assertions.assertEquals(12, templatesData.size());
        Assertions.assertEquals(6, templatesData.stream().filter(templateData -> templateData.standard().equals(DOMAIN_EVENT)).count());

        Assertions.assertEquals("Author", templatesData.get(0).parameters().find(AGGREGATE_PROTOCOL_NAME));
        Assertions.assertEquals(JOURNAL, templatesData.get(1).parameters().find(STORAGE_TYPE));
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", templatesData.get(1).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(authorPackagePath, "Author.java").toString(), templatesData.get(0).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorEntity.java").toString(), templatesData.get(1).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorState.java").toString(), templatesData.get(2).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorPlaceholderDefined.java").toString(), templatesData.get(3).file().getAbsolutePath());

        Assertions.assertEquals("AuthorAccepted", templatesData.get(4).parameters().find(DOMAIN_EVENT_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", templatesData.get(4).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorAccepted.java").toString(), templatesData.get(4).file().getAbsolutePath());

        Assertions.assertEquals("AuthorRegistered", templatesData.get(5).parameters().find(DOMAIN_EVENT_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.model.author", templatesData.get(5).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(authorPackagePath, "AuthorRegistered.java").toString(), templatesData.get(5).file().getAbsolutePath());

        Assertions.assertEquals("Book", templatesData.get(6).parameters().find(AGGREGATE_PROTOCOL_NAME));
        Assertions.assertEquals(JOURNAL, templatesData.get(7).parameters().find(STORAGE_TYPE));
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", templatesData.get(7).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(bookPackagePath, "Book.java").toString(), templatesData.get(6).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookEntity.java").toString(), templatesData.get(7).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookState.java").toString(), templatesData.get(8).file().getAbsolutePath());
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPlaceholderDefined.java").toString(), templatesData.get(9).file().getAbsolutePath());

        Assertions.assertEquals("BookPurchased", templatesData.get(10).parameters().find(DOMAIN_EVENT_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", templatesData.get(10).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPurchased.java").toString(), templatesData.get(10).file().getAbsolutePath());

        Assertions.assertEquals("BookPublished", templatesData.get(11).parameters().find(DOMAIN_EVENT_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.model.book", templatesData.get(11).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(bookPackagePath, "BookPublished.java").toString(), templatesData.get(11).file().getAbsolutePath());
    }

}
