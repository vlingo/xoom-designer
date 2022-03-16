package io.vlingo.xoom.designer.codegen.java.unittest.resource;

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
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class RestResourceWithCompositeIdUnitTestGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatResourcesUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters = codeGenerationParameters();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceUnitTestGenerationStep().process(context);

    final Content authorResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "AuthorResourceTest");
    final Content bookResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "BookResourceTest");
    Assertions.assertEquals(3, context.contents().size());
    Assertions.assertTrue(authorResourceTest.contains(TextExpectation.onJava().read("author-rest-resource-unit-test")));
    Assertions.assertTrue(bookResourceTest.contains(TextExpectation.onJava().read("book-with-composite-id-rest-resource-unit-test")));
  }

  private CodeGenerationParameters codeGenerationParameters() {
    return CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.CQRS, "true")
        .add(authorAggregate())
        .add(bookAggregate())
        .add(nameValueObject())
        .add(rankValueObject())
        .add(tagValueObject())
        .add(classificationValueObject())
        .add(classifierValueObject());
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
            .relate(Label.FIELD_TYPE, "LocalDate");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(idField).relate(nameField);

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(idField).relate(rankField);

    final CodeGenerationParameter authorTaggedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

    final CodeGenerationParameter authorUntaggedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUntagged")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags"));

    final CodeGenerationParameter authorRelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

    final CodeGenerationParameter authorUnrelatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors"));

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

    final CodeGenerationParameter withNameRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/authors/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    final CodeGenerationParameter changeRankRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "changeRank")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/authors/{id}/rank")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter addTagMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTag")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter addTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter replaceTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter removeTagMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "removeTag")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "tag")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUnrelatedEvent);

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
            .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorsReplacementMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorRelatedEvent);

    final CodeGenerationParameter relatedAuthorRemovalMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "relatedAuthors")
                .relate(Label.ALIAS, "relatedAuthor")
                .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
            .relate(authorUnrelatedEvent);

    final CodeGenerationParameter addTagRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTag")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/{id}/tag")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter addTagsRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addTags")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/{id}/tags")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter replaceTagsRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "replaceTags")
            .relate(Label.ROUTE_METHOD, "PUT")
            .relate(Label.ROUTE_PATH, "/{id}/tags")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter removeTagsRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "removeTag")
            .relate(Label.ROUTE_METHOD, "DELETE")
            .relate(Label.ROUTE_PATH, "/{id}/tags")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "relateAuthor")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/{id}/related-author")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorsRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "relateAuthors")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/{id}/related-authors")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorsReplacementRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "replaceAllRelatedAuthors")
            .relate(Label.ROUTE_METHOD, "PUT")
            .relate(Label.ROUTE_PATH, "/{id}/related-authors")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter relatedAuthorRemovalRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "unrelateAuthor")
            .relate(Label.ROUTE_METHOD, "DELETE")
            .relate(Label.ROUTE_PATH, "/{id}/related-author")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(Label.URI_ROOT, "/authors").relate(idField)
        .relate(nameField).relate(rankField).relate(availableOnField)
        .relate(tagsField).relate(relatedAuthorsField).relate(factoryMethod)
        .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(addTagMethod)
        .relate(addTagsMethod).relate(replaceTagsMethod).relate(removeTagMethod)
        .relate(relatedAuthorRoute).relate(relatedAuthorsRoute).relate(relatedAuthorsReplacementRoute)
        .relate(relatedAuthorRemovalRoute).relate(addTagRoute).relate(addTagsRoute)
        .relate(replaceTagsRoute).relate(removeTagsRoute).relate(changeRankRoute)
        .relate(relatedAuthorMethod).relate(relatedAuthorsMethod).relate(relatedAuthorRemovalMethod)
        .relate(relatedAuthorsReplacementMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
        .relate(authorTaggedEvent).relate(authorUntaggedEvent);
  }
  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter authorIdField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "authorId")
            .relate(Label.FIELD_TYPE, "CompositeId");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter bookCreatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookCreated")
            .relate(idField).relate(authorIdField).relate(nameField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "authorId")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(bookCreatedEvent);

    final CodeGenerationParameter withNameRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/authors/{authorId}/books")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(Label.URI_ROOT, "/authors/{authorId}/books").relate(idField)
        .relate(nameField).relate(authorIdField)
        .relate(factoryMethod).relate(withNameRoute)
        .relate(bookCreatedEvent);
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

  private Content[] contents() {
    return new Content[]{
        Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();
  private static final String RESOURCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "resource").toString();

  private static final String AUTHOR_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model.author; \\n" +
          "public interface Author { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model.author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_AGGREGATE_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model.author; \\n" +
          "public class AuthorEntity { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

  private static final String NAME_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Name { \\n" +
          "... \\n" +
          "}";

  private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Rank { \\n" +
          "... \\n" +
          "}";

  private static final String TAG_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Tag { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_RESOURCE_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure.resource; \\n" +
          "public interface AuthorResource { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_RESOURCE_HANDLER_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure.resource; \\n" +
          "public class AuthorResourceHandlers { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_DATA_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
