package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class EntityUnitTestGenerationStepTest extends CodeGenerationTest {

  @BeforeEach
  public void setUp() {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));
  }

  @Test
  public void testThatSourcedEntitiesUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
            .add(Label.DIALECT, Dialect.C_SHARP)
            .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content mockDispatcher = context.findContent(CsharpTemplateStandard.MOCK_DISPATCHER, "MockDispatcher");
    final Content authorEntityTest = context.findContent(CsharpTemplateStandard.ENTITY_UNIT_TEST, "AuthorEntityTest");

    Assertions.assertEquals(3, context.contents().size());
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onCSharp().read("event-based-mock-dispatcher")));
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onCSharp().read("sourced-author-entity-test")));
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
    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH,  "Io.Vlingo.Xoomapp.Model").toString();

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";
}
