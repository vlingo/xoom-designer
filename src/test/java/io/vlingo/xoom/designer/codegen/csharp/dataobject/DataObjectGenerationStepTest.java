// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.dataobject;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataObjectGenerationStepTest extends CodeGenerationTest {

  @BeforeEach
  public void setUp() {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));
  }

  @Test
  public void testThatDataObjectsAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(authorAggregate()).add(bookAggregate())
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters)
        .contents(contents());

    new DataObjectGenerationStep().process(context);

    final Content authorData = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "AuthorData");
    final Content nameDataValueObject = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "NameData");
    final Content bookDataContent = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "BookData");
    final Content rankDataContent = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "RankData");
    final Content classificationDataContent = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "ClassificationData");
    final Content classifierDataContent = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "ClassifierData");

    Assertions.assertEquals(14, context.contents().size());
    Assertions.assertTrue(authorData.contains(TextExpectation.onCSharp().read("author-data")));
    Assertions.assertTrue(bookDataContent.contains(TextExpectation.onCSharp().read("book-data")));
    Assertions.assertTrue(nameDataValueObject.contains(TextExpectation.onCSharp().read("name-data")));
    Assertions.assertTrue(rankDataContent.contains(TextExpectation.onCSharp().read("rank-data")));
    Assertions.assertTrue(classificationDataContent.contains(TextExpectation.onCSharp().read("classification-data")));
    Assertions.assertTrue(classifierDataContent.contains(TextExpectation.onCSharp().read("classifier-data")));
  }

  @Test
  public void testThatDataObjectsWithNoDuplicationAreGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(authorAggregateWithMultiNestedValueObject()).add(bookIdValueObject())
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject())
        .add(moneyValueObject()).add(retailPriceValueObject())
        .add(wholesalePriceValueObject()).add(pricingValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters)
        .contents(contents());

    new DataObjectGenerationStep().process(context);

    final Content authorData = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "AuthorData");
    final Content pricingData = context.findContent(CsharpTemplateStandard.DATA_OBJECT, "PricingData");

    Assertions.assertEquals(18, context.contents().size());
    Assertions.assertTrue(authorData.contains(TextExpectation.onCSharp().read("author-nested-value-object-data")));
    Assertions.assertTrue(pricingData.contains(TextExpectation.onCSharp().read("pricing-data")));
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

    final CodeGenerationParameter statusField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "status")
            .relate(Label.FIELD_TYPE, "boolean");

    final CodeGenerationParameter bookIds =
        CodeGenerationParameter.of(Label.STATE_FIELD, "bookIds")
            .relate(Label.FIELD_TYPE, "int")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter updatedOn =
        CodeGenerationParameter.of(Label.STATE_FIELD, "updatedOn")
            .relate(Label.FIELD_TYPE, "DateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField)
        .relate(statusField).relate(bookIds).relate(updatedOn);
  }

  private CodeGenerationParameter authorAggregateWithMultiNestedValueObject() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Rank");

    final CodeGenerationParameter pricingField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "pricing")
            .relate(Label.FIELD_TYPE, "Pricing");

    final CodeGenerationParameter statusField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "status")
            .relate(Label.FIELD_TYPE, "boolean");

    final CodeGenerationParameter bookIds =
        CodeGenerationParameter.of(Label.STATE_FIELD, "bookIds")
            .relate(Label.FIELD_TYPE, "BookId")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter updatedOn =
        CodeGenerationParameter.of(Label.STATE_FIELD, "updatedOn")
            .relate(Label.FIELD_TYPE, "DateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField).relate(pricingField)
        .relate(statusField).relate(bookIds).relate(updatedOn);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "title")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter publicationDate =
        CodeGenerationParameter.of(Label.STATE_FIELD, "publicationDate")
            .relate(Label.FIELD_TYPE, "DateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(nameField).relate(rankField).relate(publicationDate);
  }

  private CodeGenerationParameter bookIdValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "BookId")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "value")
            .relate(Label.FIELD_TYPE, "int"));
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
            .relate(Label.FIELD_TYPE, "Classifier").relate(Label.COLLECTION_TYPE, "Set"));
  }

  private CodeGenerationParameter classifierValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classifier")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String"));

  }

  private CodeGenerationParameter moneyValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Money")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "amount")
            .relate(Label.FIELD_TYPE, "int"));
  }

  private CodeGenerationParameter retailPriceValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "RetailPrice")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Money"));
  }

  private CodeGenerationParameter wholesalePriceValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "WholesalePrice")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "price")
            .relate(Label.FIELD_TYPE, "Money"));
  }

  private CodeGenerationParameter pricingValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Pricing")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "retailPrice")
            .relate(Label.FIELD_TYPE, "RetailPrice"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "wholesalePrice")
            .relate(Label.FIELD_TYPE, "WholesalePrice"));
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile("/Projects/", "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile("/Projects/", "BookState.cs"), null, null, BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Rank.cs"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Name.cs"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Classification.cs"), null, null, CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Classifier.cs"), null, null, CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("/Projects/", "IAuthor.cs"), null, null, AUTHOR_PROTOCOL_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("/Projects/", "IBook.cs"), null, null, BOOK_PROTOCOL_CONTENT_TEXT)
    };
  }

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

  private static final String AUTHOR_PROTOCOL_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_PROTOCOL_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBook { \\n" +
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

  private static final String CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Classification { \\n" +
          "... \\n" +
          "}";

  private static final String CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Classifier { \\n" +
          "... \\n" +
          "}";
}
