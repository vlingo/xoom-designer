// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.resource;

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
import io.vlingo.xoom.designer.codegen.csharp.storage.DatabaseType;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class RestResourceGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatRestResourceIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(authorAggregate()).add(nameValueObject()).add(rankValueObject())
        .add(classifierValueObject()).add(classificationValueObject())
        .add(bookAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceGenerationStep().process(context);

    final Content authorResource = context.findContent(CsharpTemplateStandard.REST_RESOURCE, "AuthorResource");
    final Content bookResource = context.findContent(CsharpTemplateStandard.REST_RESOURCE, "BookResource");

    Assertions.assertEquals(16, context.contents().size());
    Assertions.assertEquals(((TextBasedContent)authorResource).text, (TextExpectation.onCSharp().read("author-rest-resource")));
    Assertions.assertEquals(((TextBasedContent)bookResource).text, (TextExpectation.onCSharp().read("book-rest-resource")));
  }

  @Test
  public void testThatRestResourceWithQueriesIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
        .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
        .add(Label.CQRS, true)
        .add(Label.COMMAND_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(Label.QUERY_MODEL_DATABASE, DatabaseType.IN_MEMORY)
        .add(authorAggregate()).add(nameValueObject()).add(rankValueObject())
        .add(classifierValueObject()).add(classificationValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceGenerationStep().process(context);

    final Content authorResource = context.findContent(CsharpTemplateStandard.REST_RESOURCE, "AuthorResource");

    Assertions.assertEquals(15, context.contents().size());
    Assertions.assertTrue(authorResource.contains(TextExpectation.onCSharp().read("author-rest-resource-with-queries")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField = CodeGenerationParameter.of(Label.STATE_FIELD, "id")
        .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField = CodeGenerationParameter.of(Label.STATE_FIELD, "name")
        .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField = CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
        .relate(Label.FIELD_TYPE, "Rank");

    final CodeGenerationParameter tagsField = CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
        .relate(Label.FIELD_TYPE, "Tag")
        .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter relatedAuthorsField = CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
        .relate(Label.FIELD_TYPE, "String")
        .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter availableOnField = CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
        .relate(Label.FIELD_TYPE, "LocalDate");

    final CodeGenerationParameter authorRegisteredEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "AuthorRegistered")
        .relate(idField).relate(nameField);

    final CodeGenerationParameter authorRankedEvent = CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
        .relate(idField).relate(rankField);

    final CodeGenerationParameter authorTaggedEvent = CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagged")
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

    final CodeGenerationParameter authorUntaggedEvent = CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUntagged")
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

    final CodeGenerationParameter authorRelatedEvent = CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

    final CodeGenerationParameter authorUnrelatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "AuthorUnrelated")
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

    final CodeGenerationParameter factoryMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
        .relate(Label.METHOD_PARAMETER, "name")
        .relate(Label.METHOD_PARAMETER, "availableOn")
        .relate(Label.FACTORY_METHOD, "true")
        .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
        .relate(Label.METHOD_PARAMETER, "rank")
        .relate(authorRankedEvent);

    final CodeGenerationParameter unavailableMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD,
        "makeUnavailable");

    final CodeGenerationParameter withNameRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
        .relate(Label.ROUTE_METHOD, "POST")
        .relate(Label.ROUTE_PATH, "/authors/")
        .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    final CodeGenerationParameter changeRankRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "changeRank")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/authors/{id}/rank")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter addTagMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTag")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
            .relate(Label.ALIAS, "tag")
            .relate(Label.COLLECTION_MUTATION, "ADDITION"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter addTagsMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
            .relate(Label.ALIAS, "")
            .relate(Label.COLLECTION_MUTATION, "MERGE"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter replaceTagsMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
            .relate(Label.ALIAS, "")
            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter removeTagMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "removeTag")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
            .relate(Label.ALIAS, "tag")
            .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
        .relate(authorUnrelatedEvent);

    final CodeGenerationParameter relatedAuthorMethod = CodeGenerationParameter
        .of(Label.AGGREGATE_METHOD, "relateAuthor")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
            .relate(Label.ALIAS, "relatedAuthor")
            .relate(Label.COLLECTION_MUTATION, "ADDITION"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorsMethod = CodeGenerationParameter
        .of(Label.AGGREGATE_METHOD, "relateAuthors")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
            .relate(Label.ALIAS, "")
            .relate(Label.COLLECTION_MUTATION, "MERGE"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorsReplacementMethod = CodeGenerationParameter
        .of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
            .relate(Label.ALIAS, "")
            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
        .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorRemovalMethod = CodeGenerationParameter
        .of(Label.AGGREGATE_METHOD, "unrelateAuthor")
        .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
            .relate(Label.ALIAS, "relatedAuthor")
            .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
        .relate(authorUnrelatedEvent);

    final CodeGenerationParameter addTagRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTag")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/{id}/tag")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter addTagsRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTags")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/{id}/tags")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter replaceTagsRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "replaceTags")
        .relate(Label.ROUTE_METHOD, "PUT")
        .relate(Label.ROUTE_PATH, "/{id}/tags")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter removeTagsRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "removeTag")
        .relate(Label.ROUTE_METHOD, "DELETE")
        .relate(Label.ROUTE_PATH, "/{id}/tags")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "relateAuthor")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/{id}/related-author")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorsRoute = CodeGenerationParameter
        .of(Label.ROUTE_SIGNATURE, "relateAuthors")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/{id}/related-authors")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorsReplacementRoute = CodeGenerationParameter
        .of(Label.ROUTE_SIGNATURE, "replaceAllRelatedAuthors")
        .relate(Label.ROUTE_METHOD, "PUT")
        .relate(Label.ROUTE_PATH, "/{id}/related-authors")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorRemovalRoute = CodeGenerationParameter
        .of(Label.ROUTE_SIGNATURE, "unrelateAuthor")
        .relate(Label.ROUTE_METHOD, "DELETE")
        .relate(Label.ROUTE_PATH, "/{id}/related-author")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter unavailableRoute = CodeGenerationParameter
        .of(Label.ROUTE_SIGNATURE, "makeUnavailable")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/{id}/status")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(Label.URI_ROOT, "/authors").relate(idField)
        .relate(nameField).relate(rankField).relate(availableOnField)
        .relate(tagsField).relate(relatedAuthorsField).relate(factoryMethod)
        .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(addTagMethod)
        .relate(addTagsMethod).relate(replaceTagsMethod).relate(removeTagMethod).relate(unavailableMethod)
        .relate(relatedAuthorRoute).relate(relatedAuthorsRoute).relate(relatedAuthorsReplacementRoute)
        .relate(relatedAuthorRemovalRoute).relate(addTagRoute).relate(addTagsRoute)
        .relate(replaceTagsRoute).relate(removeTagsRoute).relate(changeRankRoute).relate(unavailableRoute)
        .relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
        .relate(relatedAuthorsReplacementMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
        .relate(authorTaggedEvent).relate(authorUntaggedEvent);
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
  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));}
    private CodeGenerationParameter classificationValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classification")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "label")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiers")
            .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
  }
  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter titleField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publisherField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publicationDateField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publicationDate")
            .relate(Label.FIELD_TYPE, "DateTime");

    final CodeGenerationParameter bookCreatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "BookCreated")
        .relate(idField).relate(titleField).relate(publisherField).relate(publicationDateField);

    final CodeGenerationParameter factoryMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "create")
        .relate(Label.METHOD_PARAMETER, "title")
        .relate(Label.METHOD_PARAMETER, "publisher")
        .relate(Label.METHOD_PARAMETER, "publicationDate")
        .relate(Label.FACTORY_METHOD, "true")
        .relate(bookCreatedEvent);

    final CodeGenerationParameter createRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "create")
        .relate(Label.ROUTE_METHOD, "POST")
        .relate(Label.ROUTE_PATH, "/books/")
        .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(titleField).relate(publisherField).relate(publicationDateField)
        .relate(factoryMethod).relate(bookCreatedEvent).relate(createRoute);
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "IAuthor.cs"), null, null,
            AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.cs"), null, null,
            AUTHOR_AGGREGATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.cs"), null, null,
            AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.cs"), null, null,
            RANK_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Name.cs"), null, null,
            NAME_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Tag.cs"), null, null,
            TAG_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT,
            new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null,
            AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.QUERIES,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "IAuthorQueries.cs"), null, null,
            AUTHOR_QUERIES_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.QUERIES_ACTOR,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorQueriesActor.cs"), null, null,
            AUTHOR_QUERIES_ACTOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.STORE_PROVIDER,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "QueryModelStateStoreProvider.cs"), null,
            null, QUERY_MODEL_STORE_PROVIDER_CONTENT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "IBook.cs"), null, null,
            BOOK_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookEntity.cs"), null, null,
            BOOK_AGGREGATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.cs"), null, null,
            BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT,
            new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.cs"), null, null,
            BOOK_DATA_CONTENT_TEXT),
    };
  }

  private static final String PROJECT_PATH = OperatingSystem.detect().isWindows()
      ? Paths.get("D:\\projects", "xoom-app").toString()
      : Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH = Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH = Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH = Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "Persistence")
      .toString();

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

  private static final String AUTHOR_AGGREGATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorEntity { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

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

  private static final String TAG_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Tag { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_QUERIES_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public interface IAuthorQueries { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_QUERIES_ACTOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public class AuthorQueriesActor { \\n" +
          "... \\n" +
          "}";

  private static final String QUERY_MODEL_STORE_PROVIDER_CONTENT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public class QueryModelStateStoreProvider { \\n" +
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

  private static final String BOOK_AGGREGATE_CONTENT_TEXT =
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
