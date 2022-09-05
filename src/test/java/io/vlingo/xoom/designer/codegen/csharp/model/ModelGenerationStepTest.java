package io.vlingo.xoom.designer.codegen.csharp.model;

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
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ModelGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatAggregateStatefulEntityModelIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
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
    final Content authorShortDescriptionChanged = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorShortDescriptionChanged");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-protocol")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("author-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("author-state")));
    Assertions.assertTrue(authorRegistered.contains(TextExpectation.onCSharp().read("author-registered")));
    Assertions.assertTrue(authorRanked.contains(TextExpectation.onCSharp().read("author-ranked")));
    Assertions.assertTrue(authorShortDescriptionChanged.contains(TextExpectation.onCSharp().read("author-short-description-changed")));
  }

  @Test
  public void testThatAggregateStatefulEntityModelWithCollectionMutationIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
        CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
        CodeGenerationParameter.of(Label.CQRS, false),
        authorAggregateWithCollectionMutation());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(new Content[]{});

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content authorProtocol = context.findContent(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "IAuthor");
    final Content authorEntity = context.findContent(CsharpTemplateStandard.AGGREGATE, "AuthorEntity");
    final Content authorState = context.findContent(CsharpTemplateStandard.AGGREGATE_STATE, "AuthorState");

    Assertions.assertEquals(8, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-with-collection-mutation-protocol")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("author-with-collection-mutation-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("author-with-collection-mutation-state")));
  }

  @Test
  public void testThatOperationBasedStatefulSingleModelIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.NONE),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
            CodeGenerationParameter.of(Label.CQRS, false),
            authorAggregateWithValueObjects(), nameValueObject(), rankValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content authorProtocol = context.findContent(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "IAuthor");
    final Content authorEntity = context.findContent(CsharpTemplateStandard.AGGREGATE, "AuthorEntity");
    final Content authorState = context.findContent(CsharpTemplateStandard.AGGREGATE_STATE, "AuthorState");
    final Content authorRegistered = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRegistered");
    final Content authorRanked = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRanked");
    final Content authorRelated = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRelated");
    final Content authorsRelated = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorsRelated");
    final Content authorUnrelated = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorUnrelated");

    Assertions.assertEquals(10, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-protocol-for-single-model")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("operation-based-stateful-single-model-author-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("stateful-author-state")));
    Assertions.assertTrue(authorRegistered.contains(TextExpectation.onCSharp().read("author-registered")));
    Assertions.assertTrue(authorRanked.contains(TextExpectation.onCSharp().read("author-ranked-with-value-object")));
    Assertions.assertTrue(authorRelated.contains(TextExpectation.onCSharp().read("author-related")));
    Assertions.assertTrue(authorsRelated.contains(TextExpectation.onCSharp().read("authors-related")));
    Assertions.assertTrue(authorUnrelated.contains(TextExpectation.onCSharp().read("author-unrelated")));
  }

  @Test
  public void testThatSourcedSingleModelIsGenerated()  {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.JOURNAL),
            CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
            CodeGenerationParameter.of(Label.CQRS, true),
            authorAggregateWithValueObjects(), nameValueObject(), rankValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content authorProtocol = context.findContent(CsharpTemplateStandard.AGGREGATE_PROTOCOL, "IAuthor");
    final Content authorEntity = context.findContent(CsharpTemplateStandard.AGGREGATE, "AuthorEntity");
    final Content authorState = context.findContent(CsharpTemplateStandard.AGGREGATE_STATE, "AuthorState");
    final Content authorRegistered = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRegistered");
    final Content authorRanked = context.findContent(CsharpTemplateStandard.DOMAIN_EVENT, "AuthorRanked");

    Assertions.assertEquals(10, context.contents().size());
    Assertions.assertTrue(authorProtocol.contains(TextExpectation.onCSharp().read("author-protocol-for-single-model-with-cqrs")));
    Assertions.assertTrue(authorEntity.contains(TextExpectation.onCSharp().read("sourced-single-model-author-entity")));
    Assertions.assertTrue(authorState.contains(TextExpectation.onCSharp().read("sourced-author-state")));
    Assertions.assertTrue(authorRegistered.contains(TextExpectation.onCSharp().read("author-registered")));
    Assertions.assertTrue(authorRanked.contains(TextExpectation.onCSharp().read("author-ranked-with-value-object")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "Id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "Name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter shortDescriptionField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "shortDescription")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "Rank")
            .relate(Label.FIELD_TYPE, "Double")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "Id"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "Id"))
            .relate(rankField);

    final CodeGenerationParameter authorShortDescriptionChangedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorShortDescriptionChanged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "Id"))
            .relate(shortDescriptionField);

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
        .relate(idField).relate(nameField).relate(rankField).relate(shortDescriptionField)
        .relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(authorShortDescriptionChangedEvent);
  }

  private CodeGenerationParameter authorAggregateWithCollectionMutation() {
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

    final CodeGenerationParameter availableOnField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
            .relate(Label.FIELD_TYPE, "DateTime");

    final CodeGenerationParameter relatedAuthors =
        CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
            .relate(Label.FIELD_TYPE, "String")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(rankField);

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(Label.FACTORY_METHOD, "true")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
            .relate(Label.METHOD_PARAMETER, "rank")
            .relate(authorRankedEvent);

    final CodeGenerationParameter relatedAuthorMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthors")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"))
            .relate(authorsRelatedEvent);

    final CodeGenerationParameter relatedAuthorsReplacementMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorsRelatedEvent);

    final CodeGenerationParameter relatedAuthorRemovalMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUnrelatedEvent);

    final CodeGenerationParameter hideMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "hide");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField).relate(relatedAuthors)
        .relate(availableOnField).relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
        .relate(relatedAuthorsReplacementMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
        .relate(authorRelatedEvent).relate(authorsRelatedEvent).relate(authorUnrelatedEvent);
  }

  private CodeGenerationParameter authorAggregateWithValueObjects() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter availableOnField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
            .relate(Label.FIELD_TYPE, "LocalDate");

    final CodeGenerationParameter relatedAuthors =
        CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
            .relate(Label.FIELD_TYPE, "String")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(rankField);

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
            .relate(Label.METHOD_PARAMETER, "rank")
            .relate(authorRankedEvent);

    final CodeGenerationParameter relatedAuthorMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthors")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"))
            .relate(authorsRelatedEvent);

    final CodeGenerationParameter relatedAuthorsReplacementMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorsRelatedEvent);

    final CodeGenerationParameter relatedAuthorRemovalMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUnrelatedEvent);

    final CodeGenerationParameter hideMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "hide");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField).relate(relatedAuthors)
        .relate(availableOnField).relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
        .relate(relatedAuthorsReplacementMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
        .relate(authorRelatedEvent).relate(authorsRelatedEvent).relate(authorUnrelatedEvent);
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
            .relate(Label.FIELD_TYPE, "String").relate(Label.COLLECTION_TYPE, "Set"));
  }
  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.cs"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Name.cs"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT)
    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Model").toString();

  private static final String NAME_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Name { \\n" +
          "... \\n" +
          "}";

  private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Rank { \\n" +
          "... \\n" +
          "}";

}
