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

public class RestResourceUnitTestGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatAbstractResourcesUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters = codeGenerationParameters();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceAbstractUnitTestGenerationStep().process(context);

    final Content abstractRestTest =
        context.findContent(JavaTemplateStandard.ABSTRACT_REST_RESOURCE_UNIT_TEST, "AbstractRestTest");
    Assertions.assertEquals(2, context.contents().size());
    Assertions.assertTrue(abstractRestTest.contains(TextExpectation.onJava().read("abstract-rest-resource-unit-test")));
  }

  @Test
  public void testThatResourcesUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters = codeGenerationParameters();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceUnitTestGenerationStep().process(context);

    final Content authorResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "AuthorResourceTest");
    Assertions.assertEquals(2, context.contents().size());
    Assertions.assertTrue(authorResourceTest.contains(TextExpectation.onJava().read("author-rest-resource-unit-test")));
  }

  @Test
  public void testThatResourcesUnitTestsWithDisabledTestsAreGenerated() {
    final CodeGenerationParameters parameters = codeGenerationParametersWithMultiUriPath();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceUnitTestGenerationStep().process(context);

    final Content roleResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "RoleResourceTest");
    Assertions.assertEquals(2, context.contents().size());
    Assertions.assertTrue(roleResourceTest.contains(TextExpectation.onJava().read("role-rest-resource-unit-test")));
  }

  @Test
  public void testThatResourcesUnitTestsWithMethodParametersNotMapAggregateStateFieldsAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.CQRS, "true")
        .add(productWithMinimalMethodParamsAggregate());
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceUnitTestGenerationStep().process(context);

    final Content productResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "ProductResourceTest");
    Assertions.assertEquals(2, context.contents().size());
    Assertions.assertTrue(productResourceTest.contains(TextExpectation.onJava().read("product-with-minimal-method-params-rest-resource-unit-test")));
  }


  @Test
  public void testThatResourcesUnitTestsWithToDoCommentWhenModelHasSelfDescribingEventsAreGenerated() {
    final CodeGenerationParameters parameters = codeGenerationParametersWithSelfDescribingEvents();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceUnitTestGenerationStep().process(context);

    final Content productResourceTest =
        context.findContent(JavaTemplateStandard.REST_RESOURCE_UNIT_TEST, "ProductResourceTest");
    Assertions.assertEquals(2, context.contents().size());
    Assertions.assertTrue(productResourceTest.contains(TextExpectation.onJava().read("product-rest-resource-unit-test")));
  }

  private CodeGenerationParameters codeGenerationParameters() {
    return CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.CQRS, "true")
        .add(authorAggregate())
        .add(nameValueObject())
        .add(rankValueObject())
        .add(tagValueObject())
        .add(classificationValueObject())
        .add(classifierValueObject());
  }

  private CodeGenerationParameters codeGenerationParametersWithMultiUriPath() {
    return CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.CQRS, "true")
        .add(roleAggregate());
  }

  private CodeGenerationParameters codeGenerationParametersWithSelfDescribingEvents() {
    return CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.CQRS, "true")
        .add(productAggregate());
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
            .relate(authorTaggedEvent);

    final CodeGenerationParameter addTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "MERGE"))
            .relate(authorTaggedEvent);

    final CodeGenerationParameter replaceTagsMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tags")
                .relate(Label.ALIAS, "")
                .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
            .relate(authorTaggedEvent);

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

  private CodeGenerationParameter roleAggregate() {

    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");
            
    final CodeGenerationParameter tenantIdField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "tenantId")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter descriptionField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "description")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter roleProvisionedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "RoleProvisioned")
            .relate(tenantIdField).relate(nameField);

    final CodeGenerationParameter roleDescriptionChangedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "RoleDescriptionChanged")
            .relate(tenantIdField).relate(descriptionField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "provisionRole")
            .relate(Label.METHOD_PARAMETER, "tenantId")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "description")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(roleProvisionedEvent);

    @SuppressWarnings("unused")
    final CodeGenerationParameter changeDescription =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeDescription")
            .relate(Label.METHOD_PARAMETER, "tenantId")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "description")
            .relate(roleDescriptionChangedEvent);

    final CodeGenerationParameter provisionRoleRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "provisionRole")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/tenants/{tenantId}/roles")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    final CodeGenerationParameter changeDescriptionRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "changeDescription")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/tenants/{tenantId}/roles/{roleName}/description")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    final CodeGenerationParameter changeDescriptionMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeDescription")
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "tenantId"))
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "name"))
            .relate(CodeGenerationParameter.of(Label.METHOD_PARAMETER, "description"))
            .relate(roleDescriptionChangedEvent);

    return CodeGenerationParameter.of(Label.AGGREGATE, "Role")
        .relate(Label.URI_ROOT, "/tenants").relate(idField)
        .relate(tenantIdField).relate(nameField).relate(descriptionField)
        .relate(factoryMethod).relate(changeDescriptionMethod)
        .relate(provisionRoleRoute).relate(changeDescriptionRoute)
        .relate(roleProvisionedEvent).relate(roleDescriptionChangedEvent);
  }

  private CodeGenerationParameter productAggregate() {

    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter descriptionField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "description")
            .relate(Label.FIELD_TYPE, "String");
            
    @SuppressWarnings("unused")
    final CodeGenerationParameter disabledField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "disabled")
            .relate(Label.FIELD_TYPE, "boolean");

    final CodeGenerationParameter productDefinedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ProductDefined")
            .relate(idField).relate(nameField).relate(descriptionField);

    final CodeGenerationParameter productDisabledEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ProductDisabled")
        .relate(idField);

    final CodeGenerationParameter defineMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "define")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "description")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(productDefinedEvent);

    final CodeGenerationParameter disableMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "disable")
            .relate(productDisabledEvent);

    final CodeGenerationParameter defineRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "define")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/products")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    final CodeGenerationParameter disableRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "disable")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/products/{id}/disable")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Product")
        .relate(Label.URI_ROOT, "/products").relate(idField)
        .relate(nameField).relate(descriptionField)
        .relate(defineMethod).relate(disableMethod)
        .relate(defineRoute).relate(disableRoute)
        .relate(productDefinedEvent).relate(productDisabledEvent);
  }

  private CodeGenerationParameter productWithMinimalMethodParamsAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter descriptionField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "description")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter priceField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Double");

    final CodeGenerationParameter productDefinedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ProductDefined")
            .relate(idField).relate(nameField).relate(descriptionField).relate(priceField);

    final CodeGenerationParameter priceChangedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "PriceChangedEvent")
            .relate(idField).relate(priceField);

    final CodeGenerationParameter defineMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "define")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "description")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(productDefinedEvent);

    final CodeGenerationParameter changePriceMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changePrice")
            .relate(Label.METHOD_PARAMETER, "price")
            .relate(priceChangedEvent);

    final CodeGenerationParameter defineRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "define")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/products")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Product")
        .relate(Label.URI_ROOT, "/products").relate(idField)
        .relate(nameField).relate(descriptionField).relate(priceField)
        .relate(defineMethod).relate(changePriceMethod).relate(defineRoute)
        .relate(productDefinedEvent).relate(productDefinedEvent);
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

  @SuppressWarnings("unused")
  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

  @SuppressWarnings("unused")
  private static final String RESOURCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "resource").toString();

  @SuppressWarnings("unused")
  private static final String AUTHOR_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model.author; \\n" +
          "public interface Author { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model.author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
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

  @SuppressWarnings("unused")
  private static final String NAME_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Name { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Rank { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String TAG_VALUE_OBJECT_CONTENT_TEXT =
      "package io.vlingo.xoomapp.model; \\n" +
          "public class Tag { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String AUTHOR_RESOURCE_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure.resource; \\n" +
          "public interface AuthorResource { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String AUTHOR_RESOURCE_HANDLER_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure.resource; \\n" +
          "public class AuthorResourceHandlers { \\n" +
          "... \\n" +
          "}";

  @SuppressWarnings("unused")
  private static final String BOOK_DATA_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
