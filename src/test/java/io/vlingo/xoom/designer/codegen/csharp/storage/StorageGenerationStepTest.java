package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
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

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
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

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(authorStateAdapter.contains(TextExpectation.onCSharp().read("author-state-adapter")));
    Assertions.assertTrue(commandModelStateStoreProvider.contains(TextExpectation.onCSharp().read("command-model-state-store-provider")));
    Assertions.assertTrue(queryModelStateStoreProvider.contains(TextExpectation.onCSharp().read("query-model-state-store-provider")));
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

    Assertions.assertEquals(9, context.contents().size());
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

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(authorRegisteredAdapter.contains(TextExpectation.onCSharp().read("author-registered-adapter")));
    Assertions.assertTrue(commandModelJournalProvider.contains(TextExpectation.onCSharp().read("command-model-journal-provider")));
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

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "IAuthor").toString(), "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "AuthorState").toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTION_PACKAGE_PATH, "AuthorData").toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "AuthorRegistered").toString(), "AuthorRegistered.cs"), null, null, AUTHOR_REGISTERED_CONTENT_TEXT)
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
}
