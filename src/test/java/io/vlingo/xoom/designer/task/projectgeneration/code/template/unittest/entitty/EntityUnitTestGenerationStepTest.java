// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.queries.QueriesUnitTestGenerationStep;
import io.vlingo.xoom.turbo.OperatingSystem;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.TextExpectation;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.turbo.codegen.template.OutputFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.FACTORY_METHOD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.PACKAGE;


public class EntityUnitTestGenerationStepTest {

  @Test
  public void testThatSourcedEntitiesUnitTestsAreGenerated() throws IOException {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.LANGUAGE, Language.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
                    .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("sourced-author-entity-test")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("sourced-book-entity-test")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("event-based-mock-dispatcher")));
  }

  @Test
  public void testThatStatefulEntitiesWithEventBasedProjectionUnitTestsAreGenerated() throws IOException {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.LANGUAGE, Language.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
                    .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("stateful-author-entity-test-with-event-based-projection")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("stateful-book-entity-test-with-event-based-projection")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("event-based-mock-dispatcher")));
  }

  @Test
  public void testThatStatefulEntitiesWithOperationBasedProjectionUnitTestsAreGenerated() throws IOException {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.LANGUAGE, Language.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
                    .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("stateful-author-entity-test-with-operation-based-projection")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("stateful-book-entity-test-with-operation-based-projection")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("operation-based-mock-dispatcher")));
  }

  @Test
  public void testThatQueriesUnitTestGenerationShouldProcess() {
    final CodeGenerationContext context =
            CodeGenerationContext.with(CodeGenerationParameters.from(Label.CQRS, true));

    Assertions.assertTrue(new QueriesUnitTestGenerationStep().shouldProcess(context));
  }

  @Test
  public void testThatQueriesUnitTestGenerationShouldNotProcess() {
    final CodeGenerationContext context =
            CodeGenerationContext.with(CodeGenerationParameters.from(Label.CQRS, false));

    Assertions.assertFalse(new QueriesUnitTestGenerationStep().shouldProcess(context));
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter authorRegisteredEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "name"));

    final CodeGenerationParameter authorRankedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "rank"));

    final CodeGenerationParameter factoryMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                    .relate(Label.METHOD_PARAMETER, "name")
                    .relate(FACTORY_METHOD, "true")
                    .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                    .relate(Label.METHOD_PARAMETER, "rank")
                    .relate(FACTORY_METHOD, "false")
                    .relate(authorRankedEvent);

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

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
            .relate(factoryMethod).relate(rankMethod).relate(authorRegisteredEvent).relate(authorRankedEvent)
            .relate(idField).relate(nameField).relate(rankField).relate(statusField);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter bookCatalogedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookCataloged")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "title"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "publicher"));

    final CodeGenerationParameter catalogMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "catalog")
                    .relate(Label.METHOD_PARAMETER, "title")
                    .relate(Label.METHOD_PARAMETER, "publisher")
                    .relate(FACTORY_METHOD, "false")
                    .relate(bookCatalogedEvent);

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
            .relate(bookCatalogedEvent).relate(catalogMethod)
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
    return new Content[] {
            Content.with(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
            Content.with(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), null, null, BOOK_CONTENT_TEXT),
            Content.with(VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT)
    };
  }

  private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model; \\n" +
                  "public class Rank { \\n" +
                  "... \\n" +
                  "}";

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

  @SuppressWarnings("unused")
  private static final String PERSISTENCE_PACKAGE_PATH =
          Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

  private static final String AUTHOR_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.author; \\n" +
                  "public interface Author { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_CONTENT_TEXT =
          "package io.vlingo.xoomapp.model.book; \\n" +
                  "public interface Book { \\n" +
                  "... \\n" +
                  "}";

  @SuppressWarnings("unused")
  private static final String AUTHOR_REGISTERED_ADAPTER_CONTENT_TEXT =
          "package io.vlingo.xoomapp.infrastructure; \\n" +
                  "public class AuthorRegisteredAdapter { \\n" +
                  "... \\n" +
                  "}";

  @SuppressWarnings("unused")
  private static final String AUTHOR_RANKED_ADAPTER_CONTENT_TEXT =
          "package io.vlingo.xoomapp.infrastructure; \\n" +
                  "public class AuthorRankedAdapter { \\n" +
                  "... \\n" +
                  "}";

  @SuppressWarnings("unused")
  private static final String BOOK_CATALOGED_ADAPTER_CONTENT_TEXT =
          "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                  "public interface AuthorQueries { \\n" +
                  "... \\n" +
                  "}";

}
