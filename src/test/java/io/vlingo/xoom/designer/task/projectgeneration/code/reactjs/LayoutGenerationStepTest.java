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

import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.ReactJsTemplateStandard.*;

public class LayoutGenerationStepTest {

  @Test
  public void testThatLayoutIsGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(
                    CodeGenerationParameter.of(Label.ARTIFACT_ID, "xoom-app"),
                    authorAggregate(), nameValueObject(), rankValueObject(),
                    classificationValueObject(), classifierValueObject()
            );

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new LayoutGenerationStep().process(context);

    final Content app = context.findContent(APP, "App");
    final Content sidebar = context.findContent(SIDEBAR, "Sidebar");
    final Content header = context.findContent(HEADER, "Header");

    Assertions.assertTrue(app.contains(TextExpectation.onReactJs().read("app")));
    Assertions.assertTrue(sidebar.contains(TextExpectation.onReactJs().read("sidebar")));
    Assertions.assertTrue(header.contains(TextExpectation.onReactJs().read("header")));
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
