package io.vlingo.xoom.designer.codegen.csharp.model;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ModelGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatAggregateStatefulEntityModelIsGenerated()  {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));

    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
                    CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
                    CodeGenerationParameter.of(Label.CQRS, false),
                    authorAggregate());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content authorProtocol = context.findContent(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "IAuthor");
    final Content authorEntity = context.findContent(CsharpTemplateStandard.AGGREGATE, "AuthorEntity");
    final Content authorState = context.findContent(CsharpTemplateStandard.AGGREGATE_STATE, "AuthorState");

    Assertions.assertEquals(3, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-protocol")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("author-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("author-state")));
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
    };
  }

  private static final String PROJECT_PATH =
          OperatingSystem.detect().isWindows() ?
                  Paths.get("D:\\projects", "xoom-app").toString() :
                  Paths.get("/home", "xoom-app").toString();

}
