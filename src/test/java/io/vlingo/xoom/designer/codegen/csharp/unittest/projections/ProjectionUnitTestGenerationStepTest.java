package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

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

public class ProjectionUnitTestGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatEntityStateProjectionsUnitTestAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.PROJECTION_TYPE, ProjectionType.NONE)
        .add(authorAggregate()).add(nameValueObject()).add(rankValueObject())
        .add(tagValueObject()).add(classificationValueObject()).add(classifierValueObject());
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new ProjectionUnitTestGenerationStep().process(context);

    final Content authorProjectionTest =
        context.findContent(CsharpTemplateStandard.PROJECTION_UNIT_TEST, "AuthorProjectionTest");

    final Content countingProjectionControl =
        context.findContent(CsharpTemplateStandard.COUNTING_PROJECTION_CTL, "CountingProjectionControl");
    final Content countingReadResultInterest =
        context.findContent(CsharpTemplateStandard.COUNTING_READ_RESULT, "CountingReadResultInterest");

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(countingProjectionControl.contains(TextExpectation.onCSharp().read("counting-projection-control")));
    Assertions.assertTrue(countingReadResultInterest.contains(TextExpectation.onCSharp().read("counting-read-result-interest")));
    Assertions.assertTrue(authorProjectionTest.contains(TextExpectation.onCSharp().read("author-no-projection-unit-test")));
  }

  @Test
  public void testThatEntityStateOperationBasedProjectionsUnitTestAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
        .add(authorAggregate()).add(nameValueObject()).add(rankValueObject())
        .add(tagValueObject()).add(classificationValueObject()).add(classifierValueObject());
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new ProjectionUnitTestGenerationStep().process(context);

    final Content authorProjectionTest =
        context.findContent(CsharpTemplateStandard.PROJECTION_UNIT_TEST, "AuthorProjectionTest");

    final Content countingProjectionControl =
        context.findContent(CsharpTemplateStandard.COUNTING_PROJECTION_CTL, "CountingProjectionControl");
    final Content countingReadResultInterest =
        context.findContent(CsharpTemplateStandard.COUNTING_READ_RESULT, "CountingReadResultInterest");

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(countingProjectionControl.contains(TextExpectation.onCSharp().read("counting-projection-control")));
    Assertions.assertTrue(countingReadResultInterest.contains(TextExpectation.onCSharp().read("counting-read-result-interest")));
    Assertions.assertTrue(authorProjectionTest.contains(TextExpectation.onCSharp().read("author-operation-based-projection-unit-test")));
  }

  @Test
  public void testThatEntityStateEventBasedProjectionsUnitTestAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(authorAggregate()).add(nameValueObject()).add(rankValueObject())
        .add(tagValueObject()).add(classificationValueObject()).add(classifierValueObject());
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new ProjectionUnitTestGenerationStep().process(context);

    final Content authorProjectionTest =
        context.findContent(CsharpTemplateStandard.PROJECTION_UNIT_TEST, "AuthorProjectionTest");

    final Content countingProjectionControl =
        context.findContent(CsharpTemplateStandard.COUNTING_PROJECTION_CTL, "CountingProjectionControl");
    final Content countingReadResultInterest =
        context.findContent(CsharpTemplateStandard.COUNTING_READ_RESULT, "CountingReadResultInterest");

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(countingProjectionControl.contains(TextExpectation.onCSharp().read("counting-projection-control")));
    Assertions.assertTrue(countingReadResultInterest.contains(TextExpectation.onCSharp().read("counting-read-result-interest")));
    Assertions.assertTrue(authorProjectionTest.contains(TextExpectation.onCSharp().read("author-event-based-projection-unit-test")));
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
            .relate(Label.FIELD_TYPE, "Rank")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter tagsField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
            .relate(Label.FIELD_TYPE, "Tag")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter relatedAuthorsField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
            .relate(Label.FIELD_TYPE, "String")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter availableOnField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
            .relate(Label.FIELD_TYPE, "DateTime");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(idField).relate(nameField);

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(idField).relate(rankField);

    final CodeGenerationParameter authorTaggedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorBulkTaggedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorBulkTagged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorUntaggedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUntagged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter authorTagsReplacedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagsReplacedEvent")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"));

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter relatedAuthorsReplacedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "RelatedAuthorsReplacedEvent")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "availableOn")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
            .relate(Label.METHOD_PARAMETER, "rank")
            .relate(authorRankedEvent);

    final CodeGenerationParameter addTagMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTag")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorTaggedEvent);

    final CodeGenerationParameter addTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"))
            .relate(authorBulkTaggedEvent);

    final CodeGenerationParameter replaceTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorTagsReplacedEvent);

    final CodeGenerationParameter removeTagMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "removeTag")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUntaggedEvent);

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
            .relate(relatedAuthorsReplacedEvent);

    final CodeGenerationParameter relatedAuthorRemovalMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUnrelatedEvent);

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author").relate(idField)
        .relate(nameField).relate(rankField).relate(availableOnField).relate(tagsField).relate(relatedAuthorsField)
        .relate(factoryMethod).relate(rankMethod).relate(addTagMethod).relate(addTagsMethod).relate(replaceTagsMethod)
        .relate(removeTagMethod).relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
        .relate(relatedAuthorsReplacementMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(authorRelatedEvent).relate(authorsRelatedEvent)
        .relate(authorUnrelatedEvent).relate(relatedAuthorsReplacedEvent).relate(authorTaggedEvent).relate(authorUntaggedEvent)
        .relate(authorBulkTaggedEvent).relate(authorTagsReplacedEvent).relate(authorRegisteredEvent).relate(authorRankedEvent);
  }

  private CodeGenerationParameter nameValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private CodeGenerationParameter tagValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Tag")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "value")
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
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiers")
            .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
  }

  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Model").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "Persistence").toString();

  private static final String PERSISTENCE_SETUP_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public class PersistenceSetup { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";
  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_REGISTERED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRegistered: DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_RANKED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRanked: DomainEvent { \\n" +
          "... \\n" +
          "}";


  private static final String AUTHOR_PROJECTION_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public class AuthorProjectionActor { \\n" +
          "... \\n" +
          "}";

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRegistered.cs"), null, null, AUTHOR_REGISTERED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRanked.cs"), null, null, AUTHOR_RANKED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.PROJECTION, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorProjectionActor.cs"), null, null, AUTHOR_PROJECTION_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.PERSISTENCE_SETUP, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "PersistenceSetup.cs"), null, null, PERSISTENCE_SETUP_CONTENT_TEXT),
    };
  }
}
