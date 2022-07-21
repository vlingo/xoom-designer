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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatAggregateStatefulEntityModelIsGenerated() {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));

    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
            CodeGenerationParameter.of(Label.CQRS, false),
            authorAggregate());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(new Content[]{});

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content authorProtocol = context.findContent(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "IAuthor");
    final Content authorEntity = context.findContent(CsharpTemplateStandard.AGGREGATE, "AuthorEntity");
    final Content authorState = context.findContent(CsharpTemplateStandard.AGGREGATE_STATE, "AuthorState");
    final Content authorRegistered = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRegistered");
    final Content authorRanked = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRanked");

    Assertions.assertEquals(5, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-protocol")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("author-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("author-state")));
    Assertions.assertTrue(authorRegistered.contains(TextExpectation.onCSharp().read("author-registered")));
    Assertions.assertTrue(authorRanked.contains(TextExpectation.onCSharp().read("author-ranked")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "Name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "Rank")
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
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "WithName")
            .relate(Label.METHOD_PARAMETER, "Name")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "ChangeRank")
            .relate(Label.METHOD_PARAMETER, "Rank")
            .relate(authorRankedEvent);

    final CodeGenerationParameter hideMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "Hide");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField)
        .relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent);
  }

}
