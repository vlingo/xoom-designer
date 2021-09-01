// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.METHOD_PARAMETER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.ReactJsTemplateStandard.*;

public class AggregateManagementGenerationStepTest {

  @Test
  public void testThatAggregateManagementIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(
            authorAggregate(), nameValueObject(), rankValueObject(),
            classificationValueObject(), classifierValueObject()
        );

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Authors");
    final Content withNameMethod = context.findContent(AGGREGATE_METHOD, "AuthorWithName");
    final Content changeRankMethod = context.findContent(AGGREGATE_METHOD, "AuthorChangeRank");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Author");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("author-aggregate-list")));
    Assertions.assertTrue(withNameMethod.contains(TextExpectation.onReactJs().read("author-with-name-method")));
    Assertions.assertTrue(changeRankMethod.contains(TextExpectation.onReactJs().read("author-change-rank")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("author-detail")));
  }

  @Test
  public void testThatAggregateWithPrimitiveCollectionIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(userAggregate());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Users");
    final Content withNameMethod = context.findContent(AGGREGATE_METHOD, "UserWithName");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "User");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("user-aggregate-list")));
    Assertions.assertTrue(withNameMethod.contains(TextExpectation.onReactJs().read("user-with-name-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("user-detail")));
  }

  @Test
  public void testThatAggregateWithValueObjectCollectionIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(catalogAggregate(), itemValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Catalogs");
    final Content createMethod = context.findContent(AGGREGATE_METHOD, "CatalogCreate");
    final Content addItemMethod = context.findContent(AGGREGATE_METHOD, "CatalogAddItem");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Catalog");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("catalog-aggregate-list")));
    Assertions.assertTrue(createMethod.contains(TextExpectation.onReactJs().read("catalog-create-method")));
    Assertions.assertTrue(addItemMethod.contains(TextExpectation.onReactJs().read("catalog-add-item-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("catalog-detail")));
  }

  @Test
  public void testThatAggregateWithValueObjectCollectionOnFactoryMethodIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(orderAggregate(), orderLineValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Orders");
    final Content createMethod = context.findContent(AGGREGATE_METHOD, "OrderCreate");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Order");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("order-aggregate-list")));
    Assertions.assertTrue(createMethod.contains(TextExpectation.onReactJs().read("order-create-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("order-detail")));
  }

  @Test
  public void testThatAggregateWithTwoLevelCollectionIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(proposalAggregateWithTwoLevelCollection(), expectationsValueObject(), moneyValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Proposals");
    final Content submitForMethod = context.findContent(AGGREGATE_METHOD, "ProposalSubmitFor");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Proposal");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("proposal-aggregate-list")));
    Assertions.assertTrue(submitForMethod.contains(TextExpectation.onReactJs().read("proposal-submit-for-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("proposal-detail")));
  }

  private CodeGenerationParameter moneyValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Money")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "value")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private CodeGenerationParameter expectationsValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Expectations")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "keywords")
            .relate(Label.FIELD_TYPE, "String").relate(Label.COLLECTION_TYPE, "List"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Money"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "dueOn")
            .relate(Label.FIELD_TYPE, "DateTime"));
  }

  private CodeGenerationParameter proposalAggregateWithTwoLevelCollection() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter expectationsField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "multipleExpectations")
            .relate(Label.FIELD_TYPE, "Expectations")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter proposalSubmittedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ProposalSubmitted")
            .relate(idField).relate(expectationsField);
    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "submitFor")
            .relate(Label.METHOD_PARAMETER, "multipleExpectations")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(proposalSubmittedEvent);

    final CodeGenerationParameter submitForRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "submitFor")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/proposals/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Proposal")
        .relate(Label.URI_ROOT, "/proposals").relate(idField)
        .relate(expectationsField)
        .relate(factoryMethod)
        .relate(submitForRoute)
        .relate(proposalSubmittedEvent);
  }

  @Test
  public void testThatAggregateWithThreeLevelCollectionIsGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(proposalAggregateWithThreeLevelCollection(), expectationsWithValueObjectCollectionValueObject(), keywordsValueObject(), moneyValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Proposals");
    final Content submitForMethod = context.findContent(AGGREGATE_METHOD, "ProposalSubmitFor");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Proposal");

    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("proposals-aggregate-list")));
    Assertions.assertTrue(submitForMethod.contains(TextExpectation.onReactJs().read("proposals-submit-for-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("proposals-detail")));
  }

  private CodeGenerationParameter expectationsWithValueObjectCollectionValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Expectations")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "keywords")
            .relate(Label.FIELD_TYPE, "Keyword").relate(Label.COLLECTION_TYPE, "List"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Money"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "dueOn")
            .relate(Label.FIELD_TYPE, "DateTime"));
  }

  private CodeGenerationParameter keywordsValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Keyword")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "value")
            .relate(Label.FIELD_TYPE, "String").relate(Label.COLLECTION_TYPE, "List"));
  }

  private CodeGenerationParameter proposalAggregateWithThreeLevelCollection() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter expectationsField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "multipleExpectations")
            .relate(Label.FIELD_TYPE, "Expectations")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter proposalSubmittedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ProposalSubmitted")
            .relate(idField).relate(expectationsField);
    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "submitFor")
            .relate(Label.METHOD_PARAMETER, "multipleExpectations")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(proposalSubmittedEvent);

    final CodeGenerationParameter submitForRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "submitFor")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/proposals/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Proposal")
        .relate(Label.URI_ROOT, "/proposals").relate(idField)
        .relate(expectationsField)
        .relate(factoryMethod)
        .relate(submitForRoute)
        .relate(proposalSubmittedEvent);
  }

  @Test
  public void testThatAggregateMethodWithoutApiModelIsNotGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(
            authorAggregateWithoutApiMode(), nameValueObject(), rankValueObject(),
            classificationValueObject(), classifierValueObject()
        );

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new AggregateManagementGenerationStep().process(context);

    final Content aggregateList = context.findContent(AGGREGATE_LIST, "Authors");
    final Content withNameMethod = context.findContent(AGGREGATE_METHOD, "AuthorWithName");
    final Content aggregateDetail = context.findContent(AGGREGATE_DETAIL, "Author");

    Assertions.assertThrows(IllegalArgumentException.class, () -> context.findContent(AGGREGATE_METHOD, "AuthorChangeRank"));
    Assertions.assertTrue(aggregateList.contains(TextExpectation.onReactJs().read("author-aggregate-list")));
    Assertions.assertTrue(withNameMethod.contains(TextExpectation.onReactJs().read("author-with-name-method")));
    Assertions.assertTrue(aggregateDetail.contains(TextExpectation.onReactJs().read("author-detail-without-change-rank")));
  }

  private CodeGenerationParameter catalogAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter categoryField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "category")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter itemsField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "items")
            .relate(Label.FIELD_TYPE, "Item")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter catalogCreatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "CatalogCreated")
            .relate(idField).relate(nameField).relate(categoryField);
    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "create")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "category")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(catalogCreatedEvent);

    final CodeGenerationParameter itemAddedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "ItemAdded")
            .relate(idField).relate(itemsField);
    final CodeGenerationParameter addItemMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addItem")
            .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "items")
                .relate(Label.ALIAS, "item")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(itemAddedEvent);

    final CodeGenerationParameter createRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "create")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/catalogs/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");
    final CodeGenerationParameter addOrderLineRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "addItem")
            .relate(Label.ROUTE_METHOD, "PATCH")
            .relate(Label.ROUTE_PATH, "/catalogs/{id}/add-item")
            .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Catalog")
        .relate(Label.URI_ROOT, "/catalogs").relate(idField)
        .relate(nameField).relate(categoryField).relate(itemsField)
        .relate(factoryMethod).relate(addItemMethod)
        .relate(createRoute).relate(addOrderLineRoute)
        .relate(catalogCreatedEvent).relate(itemAddedEvent);
  }

  private CodeGenerationParameter orderAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter clientNameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "clientName")
            .relate(Label.FIELD_TYPE, "String");
    final CodeGenerationParameter clientAddressField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "clientAddress")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter orderLinesField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "orderLines")
            .relate(Label.FIELD_TYPE, "OrderLine")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter orderCreatedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "OrderCreated")
            .relate(idField).relate(orderLinesField);
    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "create")
            .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "orderLines")
                .relate(Label.ALIAS, "orderLine")
                .relate(Label.COLLECTION_MUTATION, "ADDITION"))
            .relate(Label.FACTORY_METHOD, "true")
            .relate(orderCreatedEvent);

    final CodeGenerationParameter createRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "create")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/order/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Order")
        .relate(Label.URI_ROOT, "/orders").relate(idField)
        .relate(clientNameField).relate(clientAddressField).relate(orderLinesField)
        .relate(factoryMethod)
        .relate(createRoute)
        .relate(orderCreatedEvent);
  }

  private CodeGenerationParameter itemValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Item")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Double"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "discount")
            .relate(Label.FIELD_TYPE, "Double"));
  }

  private CodeGenerationParameter orderLineValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "OrderLine")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "productId")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "quantity")
            .relate(Label.FIELD_TYPE, "Integer"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Double"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "discount")
            .relate(Label.FIELD_TYPE, "Double"));
  }

  private CodeGenerationParameter userAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter addressField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "address")
            .relate(Label.FIELD_TYPE, "String")
            .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter userRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "UserRegistered")
            .relate(idField).relate(nameField).relate(addressField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
            .relate(Label.METHOD_PARAMETER, "address")
            .relate(Label.FACTORY_METHOD, "true")
            .relate(userRegisteredEvent);

    final CodeGenerationParameter withNameRoute =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
            .relate(Label.ROUTE_METHOD, "POST")
            .relate(Label.ROUTE_PATH, "/users/")
            .relate(Label.REQUIRE_ENTITY_LOADING, "false");

    return CodeGenerationParameter.of(Label.AGGREGATE, "User")
        .relate(Label.URI_ROOT, "/users").relate(idField)
        .relate(nameField).relate(addressField)
        .relate(factoryMethod).relate(withNameRoute)
        .relate(userRegisteredEvent);
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

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(idField).relate(nameField);

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(idField).relate(rankField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
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

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(Label.URI_ROOT, "/authors").relate(idField)
        .relate(nameField).relate(rankField).relate(factoryMethod)
        .relate(rankMethod).relate(withNameRoute).relate(changeRankRoute)
        .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(changeRankRoute);
  }

  private CodeGenerationParameter authorAggregateWithoutApiMode() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(idField).relate(nameField);

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(idField).relate(rankField);

    final CodeGenerationParameter factoryMethod =
        CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
            .relate(Label.METHOD_PARAMETER, "name")
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


    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(Label.URI_ROOT, "/authors").relate(idField)
        .relate(nameField).relate(rankField).relate(factoryMethod)
        .relate(rankMethod).relate(withNameRoute)
        .relate(authorRegisteredEvent).relate(authorRankedEvent);
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
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiers")
            .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
  }

  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));
  }
}
