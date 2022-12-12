package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.TextBasedContent;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class StorageGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testStateStoreGenerationWithoutProjections() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.NONE)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content authorStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content turboSetting = context.findContent(CsharpTemplateStandard.TURBO_SETTINGS, "xoom-turbo");

    Assertions.assertEquals(13, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertEquals(((TextBasedContent)turboSetting).text, (TextExpectation.onCSharp().read("xoom-turbo")));
  }

  @Test
  public void testStateStoreGenerationWithoutProjectionsWithMultiAggregates() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.NONE)
        .add(authorAggregate())
        .add(bookAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content bookStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "BookStateAdapter");
    final Content authorStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content stateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "StateStoreProvider");

    Assertions.assertEquals(13, context.contents().size());
    Assertions.assertEquals(((TextBasedContent)bookStateAdapter).text, (TextExpectation.onCSharp().read("book-state-adapter")));
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertEquals(((TextBasedContent)stateStoreProvider).text, (TextExpectation.onCSharp().read("state-store-provider")));
  }

  @Test
  public void testStateStoreGenerationWithProjections() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(Label.CQRS, true)
        .add(Label.COMMAND_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(Label.QUERY_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content authorStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content commandModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "CommandModelStateStoreProvider");
    final Content queryModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "QueryModelStateStoreProvider");

    Assertions.assertEquals(18, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertEquals(((TextBasedContent)commandModelStateStoreProvider).text, (TextExpectation.onCSharp().read("command-model-state-store-provider")));
    Assertions.assertEquals(((TextBasedContent)queryModelStateStoreProvider).text, (TextExpectation.onCSharp().read("query-model-state-store-provider")));
  }

  @Test
  public void testStateStoreGenerationWithOperationBasedProjections() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
        .add(Label.CQRS, true)
        .add(Label.COMMAND_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(Label.QUERY_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content authorStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content commandModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "CommandModelStateStoreProvider");
    final Content queryModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "QueryModelStateStoreProvider");

    Assertions.assertEquals(18, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertTrue(commandModelStateStoreProvider.contains(TextExpectation.onCSharp().read("command-model-state-store-provider")));
    Assertions.assertTrue(queryModelStateStoreProvider.contains(TextExpectation.onCSharp().read("query-model-state-store-provider")));
  }

  @Test
  public void testJournalStoreGenerationWithProjections() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(Label.CQRS, true)
        .add(Label.COMMAND_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(Label.QUERY_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content authorRegisteredAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorRegisteredAdapter");
    final Content commandModelJournalProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "CommandModelJournalProvider");
    final Content queryModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "QueryModelStateStoreProvider");

    Assertions.assertEquals(17, context.contents().size());
    Assertions.assertTrue(authorRegisteredAdapter.contains(TextExpectation.onCSharp().read("author-registered-adapter")));
    Assertions.assertEquals(((TextBasedContent)commandModelJournalProvider).text, (TextExpectation.onCSharp().read("command-model-journal-provider")));
    Assertions.assertTrue(queryModelStateStoreProvider.contains(TextExpectation.onCSharp().read("query-model-state-store-provider")));
  }

  @Test
  public void testStateStoreGenerationWithQueries() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
        .add(Label.CQRS, true)
        .add(Label.COMMAND_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(Label.QUERY_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new StorageGenerationStep().process(context);

    final Content authorStateAdapter = context.findContent(CsharpTemplateStandard.ADAPTER, "AuthorStateAdapter");
    final Content authorQueries = context.findContent(CsharpTemplateStandard.QUERIES, "IAuthorQueries");
    final Content authorQueriesActor = context.findContent(CsharpTemplateStandard.QUERIES_ACTOR, "AuthorQueriesActor");
    final Content commandModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "CommandModelStateStoreProvider");
    final Content queryModelStateStoreProvider = context.findContent(CsharpTemplateStandard.STORE_PROVIDER, "QueryModelStateStoreProvider");

    Assertions.assertEquals(18, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertTrue(authorQueries.contains(TextExpectation.onCSharp().read("author-queries")));
    Assertions.assertTrue(authorQueriesActor.contains(TextExpectation.onCSharp().read("author-queries-actor")));
    Assertions.assertTrue(commandModelStateStoreProvider.contains(TextExpectation.onCSharp().read("command-model-state-store-provider")));
    Assertions.assertTrue(queryModelStateStoreProvider.contains(TextExpectation.onCSharp().read("query-model-state-store-provider")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Double")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(rankField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
            .relate(Label.METHOD_PARAMETER, "rank")
            .relate(authorRankedEvent);

    final CodeGenerationParameter hideMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "hide");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField)
        .relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publicationDate =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publicationDate")
            .relate(Label.FIELD_TYPE, "DateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(nameField).relate(rankField).relate(publicationDate);
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "IAuthor").toString(), "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "AuthorState").toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "AuthorEntity").toString(), "AuthorEntity.cs"), null, null, AUTHOR_ENTITY_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTION_PACKAGE_PATH, "AuthorData").toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "AuthorRegistered").toString(), "AuthorRegistered.cs"), null, null, AUTHOR_REGISTERED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "IBook").toString(), "IBook.cs"), null, null, BOOK_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "BookState").toString(), "BookState.cs"), null, null, BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "BookEntity").toString(), "BookEntity.cs"), null, null, BOOK_ENTITY_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTION_PACKAGE_PATH, "BookData").toString(), "BookData.cs"), null, null, BOOK_DATA_CONTENT_TEXT)
    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH,  "Io.Vlingo.Xoomapp.Model").toString();
  private static final String INFRASTRUCTION_PACKAGE_PATH =
      Paths.get(PROJECT_PATH,  "Io.Vlingo.Xoomapp.Infrastructure").toString();

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_ENTITY_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorEntity { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_REGISTERED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRegistered { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBook { \\n" +
          "... \\n" +
          "}";
  private static final String BOOK_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookState { \\n" +
          "... \\n" +
          "}";
  private static final String BOOK_ENTITY_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookEntity { \\n" +
          "... \\n" +
          "}";
  private static final String BOOK_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
