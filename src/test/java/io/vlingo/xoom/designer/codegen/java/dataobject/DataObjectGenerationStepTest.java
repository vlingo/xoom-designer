// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.dataobject;

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
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataObjectGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatDataObjectsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.DIALECT, Dialect.JAVA)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters)
                    .contents(contents());

    new DataObjectGenerationStep().process(context);

    final Content authorData = context.findContent(JavaTemplateStandard.DATA_OBJECT, "AuthorData");
    final Content nameDataValueObject = context.findContent(JavaTemplateStandard.DATA_OBJECT, "NameData");
    final Content bookDataContent = context.findContent(JavaTemplateStandard.DATA_OBJECT, "BookData");
    final Content rankDataContent = context.findContent(JavaTemplateStandard.DATA_OBJECT, "RankData");
    final Content classificationDataContent = context.findContent(JavaTemplateStandard.DATA_OBJECT, "ClassificationData");
    final Content classifierDataContent = context.findContent(JavaTemplateStandard.DATA_OBJECT, "ClassifierData");

    Assertions.assertEquals(14, context.contents().size());
    Assertions.assertTrue(authorData.contains(TextExpectation.onJava().read("author-data")));
    Assertions.assertTrue(nameDataValueObject.contains(TextExpectation.onJava().read("name-data")));
    Assertions.assertTrue(bookDataContent.contains(TextExpectation.onJava().read("book-data")));
    Assertions.assertTrue(rankDataContent.contains(TextExpectation.onJava().read("rank-data")));
    Assertions.assertTrue(classificationDataContent.contains(TextExpectation.onJava().read("classification-data")));
    Assertions.assertTrue(classifierDataContent.contains(TextExpectation.onJava().read("classifier-data")));
  }

  @Test
  public void testThatDataObjectsWithNoDuplicationAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.DIALECT, Dialect.JAVA)
                    .add(authorAggregateWithMultiNestedValueObject()).add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(classifierValueObject())
                    .add(moneyValueObject()).add(retailPriceValueObject())
                    .add(wholesalePriceValueObject()).add(pricingValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters)
                    .contents(contents());

    new DataObjectGenerationStep().process(context);

    final Content authorData = context.findContent(JavaTemplateStandard.DATA_OBJECT, "AuthorData");
    final Content pricingData = context.findContent(JavaTemplateStandard.DATA_OBJECT, "PricingData");

    Assertions.assertEquals(17, context.contents().size());
    Assertions.assertEquals(((TextBasedContent) authorData).text, (TextExpectation.onJava().read("author-nested-value-object-data")));
    Assertions.assertEquals(((TextBasedContent) pricingData).text, (TextExpectation.onJava().read("pricing-data")));
    Assertions.assertTrue(pricingData.contains(TextExpectation.onJava().read("pricing-data")));
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
                    .relate(Label.FIELD_TYPE, "LocalDateTime");

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
                    .relate(Label.FIELD_TYPE, "int")
                    .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter updatedOn =
            CodeGenerationParameter.of(Label.STATE_FIELD, "updatedOn")
                    .relate(Label.FIELD_TYPE, "LocalDateTime");

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
                    .relate(Label.FIELD_TYPE, "LocalDate");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
            .relate(idField).relate(nameField).relate(rankField).relate(publicationDate);
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
            Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile("/Projects/", "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.AGGREGATE_STATE, new OutputFile("/Projects/", "BookState.java"), null, null, BOOK_STATE_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Classification.java"), null, null, CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile("/Projects/", "Classifier.java"), null, null, CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("/Projects/", "Author.java"), null, null, AUTHOR_PROTOCOL_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("/Projects/", "Book.java"), null, null, BOOK_PROTOCOL_CONTENT_TEXT)
    };
  }

  private static final String AUTHOR_STATE_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.author; \\n" +
                  "public class AuthorState { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_STATE_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.book; \\n" +
                  "public class BookState { \\n" +
                  "... \\n" +
                  "}";

  private static final String AUTHOR_PROTOCOL_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.author; \\n" +
                  "public class Author { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_PROTOCOL_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.book; \\n" +
                  "public class Book { \\n" +
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

  private static final String CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model; \\n" +
                  "public class Classification { \\n" +
                  "... \\n" +
                  "}";

  private static final String CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model; \\n" +
                  "public class Classifier { \\n" +
                  "... \\n" +
                  "}";


}
