// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;


public class QueriesUnitTestGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatQueriesUnitTestAreGenerated() {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
            .add(Label.DIALECT, Dialect.C_SHARP)
            .add(authorAggregate()).add(bookAggregate())
            .add(nameValueObject()).add(rankValueObject())
            .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    new QueriesUnitTestGenerationStep().process(context);

    final Content authorQueriesTest =
        context.findContent(CsharpTemplateStandard.QUERIES_UNIT_TEST, "AuthorQueriesTest");

    final Content bookQueriesTest =
        context.findContent(CsharpTemplateStandard.QUERIES_UNIT_TEST, "BookQueriesTest");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorQueriesTest.contains(TextExpectation.onCSharp().read("author-queries-unit-test")));
    Assertions.assertTrue(bookQueriesTest.contains(TextExpectation.onCSharp().read("book-queries-unit-test")));
  }

  @Test
  public void testThatQueriesUnitTestGenerationShouldProcess() {
    final CodeGenerationContext context =
        CodeGenerationContext.with(CodeGenerationParameters.from(Label.DIALECT, Dialect.C_SHARP)
            .add(Label.CQRS, true));

    Assertions.assertTrue(new QueriesUnitTestGenerationStep().shouldProcess(context));
  }

  @Test
  public void testThatQueriesUnitTestGenerationShouldNotProcess() {
    final CodeGenerationContext context =
        CodeGenerationContext.with(CodeGenerationParameters.from(Label.CQRS, false));

    Assertions.assertFalse(new QueriesUnitTestGenerationStep().shouldProcess(context));
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
            .relate(Label.FIELD_TYPE, "Rank")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter statusField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "status")
            .relate(Label.FIELD_TYPE, "boolean");

    final CodeGenerationParameter availableOnField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
            .relate(Label.FIELD_TYPE, "DateTime");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField).relate(statusField).relate(availableOnField);
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

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
        .relate(idField).relate(nameField).relate(rankField);
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

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "IAuthorQueries.cs"), null, null, AUTHOR_QUERIES_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.QUERIES, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "IBookQueries.cs"), null, null, BOOK_QUERIES_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "BookData.cs"), null, null, BOOK_DATA_CONTENT_TEXT),
    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "Persistence").toString();

  private static final String AUTHOR_QUERIES_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public interface IAuthorQueries { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_QUERIES_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public interface IAuthorQueries { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
