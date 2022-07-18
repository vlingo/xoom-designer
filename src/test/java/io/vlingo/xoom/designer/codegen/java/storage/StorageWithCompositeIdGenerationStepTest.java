// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class StorageWithCompositeIdGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testStateStoreGenerationWithProjections() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo")
            .add(Label.DIALECT, Dialect.JAVA)
            .add(authorAggregate()).add(bookAggregate())
            .add(nameValueObject()).add(rankValueObject())
            .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters);
    loadProperties(context, StorageType.STATE_STORE, DatabaseType.POSTGRES, ProjectionType.EVENT_BASED, true);
    loadContents(context);

    new StorageGenerationStep().process(context);

    final Content bookStateAdapter = context.findContent(JavaTemplateStandard.ADAPTER, "BookStateAdapter");
    final Content bookQueries = context.findContent(JavaTemplateStandard.QUERIES, "BookQueries");
    final Content bookQueriesActor = context.findContent(JavaTemplateStandard.QUERIES_ACTOR, "BookQueriesActor");
    final Content authorStateAdapter = context.findContent(JavaTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content authorQueries = context.findContent(JavaTemplateStandard.QUERIES, "AuthorQueries");
    final Content authorQueriesActor = context.findContent(JavaTemplateStandard.QUERIES_ACTOR, "AuthorQueriesActor");
    final Content commandModelStateStoreProvider = context.findContent(JavaTemplateStandard.STORE_PROVIDER, "CommandModelStateStoreProvider");
    final Content queryModelStateStoreProvider = context.findContent(JavaTemplateStandard.STORE_PROVIDER, "QueryModelStateStoreProvider");
    final Content databaseProperties = context.findContent(JavaTemplateStandard.DATABASE_PROPERTIES, "xoom-turbo");

    Assertions.assertEquals(20, context.contents().size());
    Assertions.assertTrue(bookStateAdapter.contains(TextExpectation.onJava().read("book-state-adapter")));
    Assertions.assertTrue(bookQueries.contains(TextExpectation.onJava().read("book-queries-with-composite-id")));
    Assertions.assertTrue(bookQueriesActor.contains(TextExpectation.onJava().read("book-queries-actor-with-composite-id")));
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onJava().read("author-state-adapter")));
    Assertions.assertTrue(authorQueries.contains(TextExpectation.onJava().read("author-queries")));
    Assertions.assertTrue(authorQueriesActor.contains(TextExpectation.onJava().read("author-queries-actor")));
    Assertions.assertTrue(commandModelStateStoreProvider.contains(TextExpectation.onJava().read("command-model-state-store-provider")));
    Assertions.assertTrue(queryModelStateStoreProvider.contains(TextExpectation.onJava().read("query-model-state-store-provider")));
    Assertions.assertTrue(databaseProperties.contains(TextExpectation.onJava().read("postgres-database-properties")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank");

    final CodeGenerationParameter statusField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "status")
            .relate(Label.FIELD_TYPE, "boolean");

    final CodeGenerationParameter bookIds =
        CodeGenerationParameter.of(Label.STATE_FIELD, "bookIds")
            .relate(Label.FIELD_TYPE, "int")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter updatedOn =
        CodeGenerationParameter.of(Label.STATE_FIELD, "updatedOn")
            .relate(Label.FIELD_TYPE, "LocalDateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField)
        .relate(statusField).relate(bookIds).relate(updatedOn);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter authorIdField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "authorId")
            .relate(Label.FIELD_TYPE, "CompositeId");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publicationDate =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publicationDate")
            .relate(Label.FIELD_TYPE, "LocalDate");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(authorIdField).relate(nameField).relate(rankField).relate(publicationDate);
  }

  private CodeGenerationParameter nameValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private CodeGenerationParameter rankValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Rank")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "points")
            .relate(Label.FIELD_TYPE, "int"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classification")
            .relate(Label.FIELD_TYPE, "Classification"));
  }

  private CodeGenerationParameter classificationValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classification")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "label")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifier")
            .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
  }

  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));

  }

  private void loadProperties(final CodeGenerationContext context,
                              final StorageType storageType,
                              final DatabaseType databaseType,
                              final ProjectionType projectionType,
                              final Boolean useCQRS) {
    context.with(Label.PACKAGE, "io.vlingo").with(Label.APPLICATION_NAME, "xoomapp")
            .with(Label.CQRS, useCQRS.toString()).with(Label.DATABASE, databaseType.name())
            .with(Label.COMMAND_MODEL_DATABASE, databaseType.name())
            .with(Label.QUERY_MODEL_DATABASE, databaseType.name())
            .with(Label.STORAGE_TYPE, storageType.name())
            .with(Label.PROJECTION_TYPE, projectionType.name())
            .with(Label.TARGET_FOLDER, HOME_DIRECTORY);
  }

  private void loadContents(final CodeGenerationContext context) {
    context.addContent(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), AUTHOR_STATE_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.java"), BOOK_STATE_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), AUTHOR_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), BOOK_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.java"), AUTHOR_ENTITY_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookEntity.java"), BOOK_ENTITY_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookRented.java"), BOOK_RENTED_TEXT);
    context.addContent(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookPurchased.java"), BOOK_PURCHASED_TEXT);
    context.addContent(JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java"), PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), AUTHOR_DATA_CONTENT_TEXT);
    context.addContent(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.java"), BOOK_DATA_CONTENT_TEXT);
  }

  private static final String HOME_DIRECTORY = OperatingSystem.detect().isWindows() ? "D:\\projects" : "/home";
  private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();
  private static final String MODEL_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "model").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "infrastructure", "persistence").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String  AUTHOR_DATA_CONTENT_TEXT =
          "package io.vlingo.xoomapp.infrastructure; \\n" +
                  "public class AuthorData { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_DATA_CONTENT_TEXT =
          "package io.vlingo.xoomapp.infrastructure; \\n" +
                  "public class BookData { \\n" +
                  "... \\n" +
                  "}";

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

  private static final String AUTHOR_ENTITY_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.author; \\n" +
                  "public interface AuthorEntity { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_ENTITY_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.book; \\n" +
                  "public interface BookEntity { \\n" +
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
