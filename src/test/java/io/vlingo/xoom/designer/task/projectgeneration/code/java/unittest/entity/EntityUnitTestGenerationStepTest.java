// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.entity;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.METHOD_PARAMETER;

public class EntityUnitTestGenerationStepTest {

  @Test
  public void testThatSourcedEntitiesUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.DIALECT, Dialect.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
                    .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(classificationValueObject()).add(tagValueObject())
                    .add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(JavaTemplateStandard.MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(8, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("sourced-author-entity-test")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("sourced-book-entity-test")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("event-based-mock-dispatcher")));
  }

  @Test
  public void testThatStatefulEntitiesWithEventBasedProjectionUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.DIALECT, Dialect.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
                    .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(tagValueObject()).add(classificationValueObject())
                    .add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(JavaTemplateStandard.MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(8, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("stateful-author-entity-test-with-event-based-projection")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("stateful-book-entity-test-with-event-based-projection")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("event-based-mock-dispatcher")));
  }

  @Test
  public void testThatStatefulEntitiesWithOperationBasedProjectionUnitTestsAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
                    .add(Label.DIALECT, Dialect.JAVA)
                    .add(Label.STORAGE_TYPE, StorageType.STATE_STORE)
                    .add(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED)
                    .add(authorAggregate()).add(bookAggregate())
                    .add(nameValueObject()).add(rankValueObject())
                    .add(tagValueObject()).add(classificationValueObject())
                    .add(classifierValueObject());

    final CodeGenerationContext context =
            CodeGenerationContext.with(parameters).contents(contents());

    new EntityUnitTestGenerationStep().process(context);

    final Content authorEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "AuthorEntityTest");

    final Content bookEntityTest =
            context.findContent(JavaTemplateStandard.ENTITY_UNIT_TEST, "BookEntityTest");

    final Content mockDispatcher =
            context.findContent(JavaTemplateStandard.MOCK_DISPATCHER, "MockDispatcher");

    Assertions.assertEquals(8, context.contents().size());
    Assertions.assertTrue(authorEntityTest.contains(TextExpectation.onJava().read("stateful-author-entity-test-with-operation-based-projection")));
    Assertions.assertTrue(bookEntityTest.contains(TextExpectation.onJava().read("stateful-book-entity-test-with-operation-based-projection")));
    Assertions.assertTrue(mockDispatcher.contains(TextExpectation.onJava().read("operation-based-mock-dispatcher")));
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

    final CodeGenerationParameter tagsField =
            CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                    .relate(Label.FIELD_TYPE, "Tag")
                    .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter relatedAuthorsField =
            CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                    .relate(Label.FIELD_TYPE, "String")
                    .relate(Label.COLLECTION_TYPE, "Set");

    final CodeGenerationParameter authorRegisteredEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "name"));

    final CodeGenerationParameter authorRankedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "rank"));

    final CodeGenerationParameter authorTaggedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagged")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                            .relate(Label.ALIAS, "tag")
                            .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorBulkTaggedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorBulkTagged")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter authorTagsReplacedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorTagsReplacedEvent")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"));

    final CodeGenerationParameter authorUntaggedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUntagged")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "tags")
                            .relate(Label.ALIAS, "tag")
                            .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter authorRelatedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRelated")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                            .relate(Label.ALIAS, "relatedAuthor")
                            .relate(Label.COLLECTION_MUTATION, "ADDITION"));

    final CodeGenerationParameter authorsRelatedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorsRelated")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "MERGE"));

    final CodeGenerationParameter relatedAuthorsReplacedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "RelatedAuthorsReplacedEvent")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"));

    final CodeGenerationParameter authorUnrelatedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorUnrelated")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                            .relate(Label.ALIAS, "relatedAuthor")
                            .relate(Label.COLLECTION_MUTATION, "REMOVAL"));

    final CodeGenerationParameter factoryMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                    .relate(Label.METHOD_PARAMETER, "name")
                    .relate(Label.METHOD_PARAMETER, "availableOn")
                    .relate(Label.FACTORY_METHOD, "true")
                    .relate(authorRegisteredEvent);

    final CodeGenerationParameter rankMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                    .relate(Label.METHOD_PARAMETER, "rank")
                    .relate(Label.FACTORY_METHOD, "false")
                    .relate(authorRankedEvent);

    final CodeGenerationParameter makeAvailableOnMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "makeAvailableOn")
                    .relate(Label.METHOD_PARAMETER, "availableOn")
                    .relate(Label.FACTORY_METHOD, "false");

    final CodeGenerationParameter deactivateMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "deactivate")
                    .relate(Label.FACTORY_METHOD, "false");

    final CodeGenerationParameter availableOnField =
            CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
                    .relate(Label.FIELD_TYPE, "LocalDate");

    final CodeGenerationParameter addTagMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTag")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                            .relate(Label.ALIAS, "tag")
                            .relate(Label.COLLECTION_MUTATION, "ADDITION"))
                    .relate(authorTaggedEvent);

    final CodeGenerationParameter addTagsMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "addTags")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "MERGE"))
                    .relate(authorBulkTaggedEvent);

    final CodeGenerationParameter replaceTagsMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceTags")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
                    .relate(authorTagsReplacedEvent);

    final CodeGenerationParameter removeTagMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "removeTag")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "tags")
                            .relate(Label.ALIAS, "tag")
                            .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
                    .relate(authorUnrelatedEvent);

    final CodeGenerationParameter relateAuthorMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthor")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                            .relate(Label.ALIAS, "relatedAuthor")
                            .relate(Label.COLLECTION_MUTATION, "ADDITION"))
                    .relate(authorRelatedEvent);

    final CodeGenerationParameter relateAuthorsMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "relateAuthors")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "MERGE"))
                    .relate(authorsRelatedEvent);

    final CodeGenerationParameter replaceAuthorsMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "replaceAllRelatedAuthors")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                            .relate(Label.ALIAS, "")
                            .relate(Label.COLLECTION_MUTATION, "REPLACEMENT"))
                    .relate(relatedAuthorsReplacedEvent);

    final CodeGenerationParameter unrelateAuthorMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "unrelateAuthor")
                    .relate(CodeGenerationParameter.of(METHOD_PARAMETER, "relatedAuthors")
                            .relate(Label.ALIAS, "relatedAuthor")
                            .relate(Label.COLLECTION_MUTATION, "REMOVAL"))
                    .relate(authorUnrelatedEvent);

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
            .relate(idField).relate(nameField).relate(rankField).relate(relatedAuthorsField)
            .relate(tagsField).relate(statusField).relate(availableOnField).relate(factoryMethod)
            .relate(rankMethod).relate(deactivateMethod).relate(makeAvailableOnMethod).relate(addTagMethod)
            .relate(addTagsMethod).relate(replaceTagsMethod).relate(removeTagMethod).relate(relateAuthorMethod)
            .relate(relateAuthorsMethod).relate(unrelateAuthorMethod).relate(replaceAuthorsMethod)
            .relate(authorRegisteredEvent).relate(authorRankedEvent).relate(authorTaggedEvent)
            .relate(authorUntaggedEvent);
  }

  private CodeGenerationParameter bookAggregate() {
    final CodeGenerationParameter bookCatalogedEvent =
            CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookCataloged")
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "title"))
                    .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "publisher"));

    final CodeGenerationParameter catalogMethod =
            CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "catalog")
                    .relate(Label.METHOD_PARAMETER, "title")
                    .relate(Label.METHOD_PARAMETER, "publisher")
                    .relate(Label.METHOD_PARAMETER, "group")
                    .relate(Label.FACTORY_METHOD, "false")
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

    final CodeGenerationParameter groupField =
            CodeGenerationParameter.of(Label.STATE_FIELD, "group")
                    .relate(Label.FIELD_TYPE, "char");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Book")
            .relate(bookCatalogedEvent).relate(catalogMethod).relate(idField)
            .relate(nameField).relate(rankField).relate(groupField);
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

  private CodeGenerationParameter tagValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Tag")
            .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
                    .relate(Label.FIELD_TYPE, "String"));
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
            Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), null, null, BOOK_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Tag.java"), null, null, TAG_VALUE_OBJECT_CONTENT_TEXT),
    };
  }

  private static final String PROJECT_PATH =
          OperatingSystem.detect().isWindows() ?
                  Paths.get("D:\\projects", "xoom-app").toString() :
                  Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "model").toString();


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

}
