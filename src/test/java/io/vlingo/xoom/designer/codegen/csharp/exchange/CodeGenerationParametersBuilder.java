// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.DeploymentType;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.DeploymentSettings;

import java.util.stream.Stream;

public class CodeGenerationParametersBuilder {

    public static Stream<CodeGenerationParameter> threeExchanges() {
        final CodeGenerationParameter dialect =
                CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP);

        final CodeGenerationParameter idField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "id")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter nameField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "Name");

        final CodeGenerationParameter rankField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
                        .relate(Label.FIELD_TYPE, "Rank");

        final CodeGenerationParameter createdOn =
                CodeGenerationParameter.of(Label.STATE_FIELD, "createdOn")
                        .relate(Label.FIELD_TYPE, "LocalDateTime");

        final CodeGenerationParameter factoryMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                        .relate(Label.METHOD_PARAMETER, "name")
                        .relate(Label.FACTORY_METHOD, "true");

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank");

        final CodeGenerationParameter blockMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "block")
                        .relate(Label.METHOD_PARAMETER, "name");

        final CodeGenerationParameter otherAppExchange =
                CodeGenerationParameter.of(Label.EXCHANGE, "book-store-exchange")
                        .relate(Label.ROLE, ExchangeRole.CONSUMER)
                        .relate(CodeGenerationParameter.of(Label.RECEIVER)
                                .relate(CodeGenerationParameter.ofObject(Label.SCHEMA, new Schema("vlingo:xoom:io.vlingo.xoom.otherapp:OtherAggregateDefined:0.0.1")))
                                .relate(Label.MODEL_METHOD, "withName"))
                        .relate(CodeGenerationParameter.of(Label.RECEIVER)
                                .relate(CodeGenerationParameter.ofObject(Label.SCHEMA, new Schema("vlingo:xoom:io.vlingo.xoom.otherapp:OtherAggregateUpdated:0.0.2")))
                                .relate(Label.MODEL_METHOD, "changeRank"))
                        .relate(CodeGenerationParameter.of(Label.RECEIVER)
                                .relate(CodeGenerationParameter.ofObject(Label.SCHEMA, new Schema("vlingo:xoom:io.vlingo.xoom.otherapp:OtherAggregateRemoved:0.0.3")))
                                .relate(Label.MODEL_METHOD, "block"));

        final CodeGenerationParameter authorExchange =
                CodeGenerationParameter.of(Label.EXCHANGE, "book-store-exchange")
                        .relate(Label.ROLE, ExchangeRole.PRODUCER)
                        .relate(Label.SCHEMA_GROUP, "vlingo:xoom:io.vlingo.xoomapp")
                        .relate(Label.DOMAIN_EVENT, "AuthorBlocked")
                        .relate(Label.DOMAIN_EVENT, "AuthorRated");

        final CodeGenerationParameter authorRatedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRated")
                        .relate(idField).relate(rankField).relate(createdOn);

        final CodeGenerationParameter authorBlockedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorBlocked")
                        .relate(nameField);

        final CodeGenerationParameter authorAggregate =
                CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                        .relate(otherAppExchange).relate(authorExchange)
                        .relate(idField).relate(nameField).relate(rankField)
                        .relate(createdOn).relate(factoryMethod).relate(rankMethod)
                        .relate(blockMethod).relate(authorRatedEvent).relate(authorBlockedEvent);

        final CodeGenerationParameter titleField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "Name");

        final CodeGenerationParameter priceField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
                        .relate(Label.FIELD_TYPE, "int");

        final CodeGenerationParameter deploymentSettings =
                CodeGenerationParameter.ofObject(Label.DEPLOYMENT_SETTINGS,
                        DeploymentSettings.with(DeploymentType.NONE, "", "", "", 8988));

        final CodeGenerationParameter bookExchange =
                CodeGenerationParameter.of(Label.EXCHANGE, "book-store-exchange")
                        .relate(Label.ROLE, ExchangeRole.PRODUCER)
                        .relate(Label.SCHEMA_GROUP, "vlingo:xoom:io.vlingo.xoomapp")
                        .relate(Label.DOMAIN_EVENT, "BookSoldOut")
                        .relate(Label.DOMAIN_EVENT, "BookPurchased");

        final CodeGenerationParameter bookSoldOutEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookSoldOut")
                        .relate(titleField);

        final CodeGenerationParameter bookPurchasedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookPurchased")
                        .relate(priceField);

        final CodeGenerationParameter bookAggregate =
                CodeGenerationParameter.of(Label.AGGREGATE, "Book")
                        .relate(titleField).relate(priceField)
                        .relate(bookSoldOutEvent).relate(bookPurchasedEvent)
                        .relate(bookExchange);

        final CodeGenerationParameter nameValueObject =
             CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
                            .relate(Label.FIELD_TYPE, "String"))
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
                            .relate(Label.FIELD_TYPE, "String"));

        final CodeGenerationParameter rankValueObject =
            CodeGenerationParameter.of(Label.VALUE_OBJECT, "Rank")
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "points")
                            .relate(Label.FIELD_TYPE, "int"))
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classification")
                            .relate(Label.FIELD_TYPE, "Classification"));

        final CodeGenerationParameter classificationValueObject =
                CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classification")
                        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "labels")
                                .relate(Label.FIELD_TYPE, "String")
                                .relate(Label.COLLECTION_TYPE, "Set"))
                        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiers")
                                .relate(Label.FIELD_TYPE, "Classifier")
                                .relate(Label.COLLECTION_TYPE, "List"))
                        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classifiedOn")
                                .relate(Label.FIELD_TYPE, "LocalDate"));

        final CodeGenerationParameter classifierValueObject =
                CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
                        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
                                .relate(Label.FIELD_TYPE, "String"));

        return Stream.of(dialect, deploymentSettings, authorAggregate, bookAggregate,
                nameValueObject, rankValueObject, classificationValueObject, classifierValueObject);
    }


}
