// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.projections;

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
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ProjectionDispatcherProviderGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatEventBasedProjectionClassesAreGeneratedForSourcedEntities() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());
    parameters.addAll(aggregates());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());
    new ProjectionGenerationStep().process(context);

    final Content bookProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "BookProjectionActor");

    final Content authorProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "AuthorProjectionActor");

    final Content dispatcherProvider =
        context.findContent(CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

    Assertions.assertEquals(((TextBasedContent) authorProjectionActor).text, (TextExpectation.onCSharp().read("event-based-author-projection-actor-for-sourced-entities")));
    Assertions.assertEquals(((TextBasedContent) bookProjectionActor).text, (TextExpectation.onCSharp().read("event-based-book-projection-actor-for-sourced-entities")));
    Assertions.assertEquals(((TextBasedContent) dispatcherProvider).text, (TextExpectation.onCSharp().read("event-based-projection-dispatcher-provider")));
  }

  @Test
  public void testThatEventBasedProjectionClassesAreGeneratedForStatefulEntities() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());
    parameters.addAll(aggregates());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());
    new ProjectionGenerationStep().process(context);

    final Content bookProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "BookProjectionActor");

    final Content authorProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "AuthorProjectionActor");

    final Content dispatcherProvider =
        context.findContent(CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

    Assertions.assertEquals(((TextBasedContent) authorProjectionActor).text, (TextExpectation.onCSharp().read("event-based-author-projection-actor-for-stateful-entities")));
    Assertions.assertEquals(((TextBasedContent) bookProjectionActor).text, (TextExpectation.onCSharp().read("event-based-book-projection-actor-for-stateful-entities")));
    Assertions.assertEquals(((TextBasedContent) dispatcherProvider).text, (TextExpectation.onCSharp().read("event-based-projection-dispatcher-provider")));
  }

  @Test
  public void testThatEventBasedProjectionClassesAreGeneratedForStatefulEntitiesWithoutFactoryMethod() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
                CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
                CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED),
                CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
                CodeGenerationParameter.of(Label.CQRS, true),
                authorAggregateWithoutFactoryMethod())
            .add(nameValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new ProjectionGenerationStep().process(context);

    final Content authorProjection = context.findContent(CsharpTemplateStandard.PROJECTION, "AuthorProjectionActor");

    Assertions.assertEquals(12, context.contents().size());

    Assertions.assertTrue(authorProjection.contains(TextExpectation.onCSharp().read("author-without-factory-method-event-based-projection-actor")));
  }

  @Test
  public void testThatOperationBasedProjectionClassesAreGenerated() {

    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());
    parameters.addAll(aggregates());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());
    new ProjectionGenerationStep().process(context);

    final Content bookProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "BookProjectionActor");

    final Content authorProjectionActor =
        context.findContent(CsharpTemplateStandard.PROJECTION, "AuthorProjectionActor");

    final Content dispatcherProvider =
        context.findContent(CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

    Assertions.assertEquals(((TextBasedContent)authorProjectionActor).text, (TextExpectation.onCSharp().read("operation-based-author-projection-actor")));
    Assertions.assertEquals(((TextBasedContent)bookProjectionActor).text, (TextExpectation.onCSharp().read("operation-based-book-projection-actor")));
    Assertions.assertEquals(((TextBasedContent)dispatcherProvider).text, (TextExpectation.onCSharp().read("operation-based-projection-dispatcher-provider")));
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.cs"), null, null, BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRegistered.cs"), null, null, AUTHOR_REGISTERED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRanked.cs"), null, null, AUTHOR_RANKED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "IBook.cs"), null, null, BOOK_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookCataloged.cs"), null, null, BOOK_CATALOGED_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.cs"), null, null, BOOK_DATA_CONTENT_TEXT)
    };
  }

  private CodeGenerationParameter authorAggregateWithoutFactoryMethod() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
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
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(nameField).relate(availableOnField)
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
                .relate(Label.FIELD_TYPE, "Rank")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "rank")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
                .relate(Label.FIELD_TYPE, "Rank")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "rank")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorBulkRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorBulkRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
                .relate(Label.FIELD_TYPE, "Rank")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addRank")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "ranks")
                .relate(Label.ALIAS, "rank")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorRankedEvent);

    final CodeGenerationParameter bulkRankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "bulkRank")
            .relate(Label.METHOD_PARAMETER, "ranks")
            .relate(Label.ALIAS, "ranks")
            .relate(Label.COLLECTION_MUTATION, "MERGE")
            .relate(authorBulkRankedEvent);

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
        .relate(availableOnField).relate(factoryMethod).relate(rankMethod).relate(bulkRankMethod)
        .relate(hideMethod).relate(relatedAuthorMethod).relate(relatedAuthorsMethod)
        .relate(relatedAuthorRemovalMethod).relate(relatedAuthorsReplacementMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(authorRelatedEvent)
        .relate(authorsRelatedEvent).relate(authorUnrelatedEvent).relate(authorBulkRankedEvent);
  }

  private CodeGenerationParameters aggregates() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
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
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(nameField).relate(availableOnField);

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
                .relate(Label.FIELD_TYPE, "Rank")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "rank")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorBulkRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorBulkRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "ranks")
                .relate(Label.FIELD_TYPE, "Rank")
                .relate(Label.COLLECTION_TYPE, "List")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                .relate(Label.FIELD_TYPE, "String")
                .relate(Label.COLLECTION_TYPE, "Set")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                .relate(Label.FIELD_TYPE, "String"))
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
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addRank")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "ranks")
                .relate(Label.ALIAS, "rank")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorRankedEvent);

    final CodeGenerationParameter bulkRankMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "bulkRank")
            .relate(Label.METHOD_PARAMETER, "ranks")
            .relate(Label.ALIAS, "ranks")
            .relate(Label.COLLECTION_MUTATION, "MERGE")
            .relate(authorBulkRankedEvent);

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

    final CodeGenerationParameter authorAggregate =
        CodeGenerationParameter.of(Label.AGGREGATE, "Author")
            .relate(idField).relate(nameField).relate(rankField).relate(relatedAuthors)
            .relate(availableOnField).relate(factoryMethod).relate(rankMethod).relate(bulkRankMethod)
            .relate(hideMethod).relate(relatedAuthorMethod).relate(relatedAuthorsMethod)
            .relate(relatedAuthorRemovalMethod).relate(relatedAuthorsReplacementMethod)
            .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(authorRelatedEvent)
            .relate(authorsRelatedEvent).relate(authorUnrelatedEvent).relate(authorBulkRankedEvent);

    final CodeGenerationParameter titleField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publisherField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter bookCatalogedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookCataloged")
            .relate(idField).relate(titleField).relate(publisherField);

    final CodeGenerationParameter catalogMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "catalog")
            .relate(Label.METHOD_PARAMETER, "title")
            .relate(Label.METHOD_PARAMETER, "publisher")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(bookCatalogedEvent);

    final CodeGenerationParameter bookAggregate =
        CodeGenerationParameter.of(Label.AGGREGATE, "Book")
            .relate(idField).relate(titleField)
            .relate(publisherField).relate(catalogMethod)
            .relate(bookCatalogedEvent);

    return CodeGenerationParameters.from(authorAggregate, bookAggregate);
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
            .relate(Label.FIELD_TYPE, "Classifier"));
  }

  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private CodeGenerationParameters codeGenerationParameters() {
    return aggregates().add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());
  }

  private static final String HOME_DIRECTORY = OperatingSystem.detect().isWindows() ? "D:\\projects" : "/home";
  private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();
  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();
  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookState { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBook { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CATALOGED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookCataloged : DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_REGISTERED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRegistered : DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_RANKED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRanked : DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
