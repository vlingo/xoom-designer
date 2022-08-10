// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.valueobject;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.model.ValueObjectGenerationStep;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValueObjectGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatValueObjectsAreGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
            authorAggregate(), bookAggregate(), nameValueObject(), rankValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    final CodeGenerationStep codeGenerationStep = new ValueObjectGenerationStep();

    Assertions.assertTrue(codeGenerationStep.shouldProcess(context));

    codeGenerationStep.process(context);

    Assertions.assertEquals(2, context.contents().size());

    final Content nameValueObject = context.findContent(CsharpTemplateStandard.VALUE_OBJECT, "Name");
    final Content rankValueObject = context.findContent(CsharpTemplateStandard.VALUE_OBJECT, "Rank");

    Assertions.assertTrue(nameValueObject.contains(TextExpectation.onCSharp().read("name-value-object")));
    Assertions.assertTrue(rankValueObject.contains(TextExpectation.onCSharp().read("rank-value-object")));
  }
  @Test
  public void testThatValueObjectsWithDateTimeAreGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "Io.Vlingo.Xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.C_SHARP),
            bookAggregateWithVolumes(), volumeValueObject(), rankValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    final CodeGenerationStep codeGenerationStep = new ValueObjectGenerationStep();

    Assertions.assertTrue(codeGenerationStep.shouldProcess(context));

    codeGenerationStep.process(context);

    Assertions.assertEquals(2, context.contents().size());

    final Content volumeValueObject = context.findContent(CsharpTemplateStandard.VALUE_OBJECT, "Volume");

    Assertions.assertTrue(volumeValueObject.contains(TextExpectation.onCSharp().read("volume-value-object")));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField = CodeGenerationParameter.of(Label.STATE_FIELD, "id")
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

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField)
        .relate(factoryMethod).relate(rankMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(nameField).relate(rankField);
  }

  private CodeGenerationParameter bookAggregateWithVolumes() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank");

    final CodeGenerationParameter volumesField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "volumes")
            .relate(Label.FIELD_TYPE, "Volume")
            .relate(Label.COLLECTION_TYPE, "List");


    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(nameField).relate(rankField).relate(volumesField);
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
            .relate(Label.FIELD_TYPE, "String"));
  }

  private CodeGenerationParameter volumeValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Volume")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "number")
            .relate(Label.FIELD_TYPE, "int"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "publicationDate")
            .relate(Label.FIELD_TYPE, "DateTime"));
  }
}
