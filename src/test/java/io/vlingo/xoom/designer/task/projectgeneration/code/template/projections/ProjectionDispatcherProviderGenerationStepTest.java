// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DOMAIN_EVENT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class ProjectionDispatcherProviderGenerationStepTest {

    private static final String HOME_DIRECTORY = OperatingSystem.detect().isWindows() ? "D:\\projects" : "/home";
    private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();
    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

    private static final String INFRASTRUCTURE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "infrastructure").toString();

    @Test
    public void testThatEventBasedProjectionClassesAreGeneratedForSourcedEntities() throws IOException {
        final CodeGenerationContext context =
                CodeGenerationContext.with(codeGenerationParameters());

        loadParameters(context, StorageType.JOURNAL.name(), ProjectionType.EVENT_BASED.name());
        loadContents(context);
        new ProjectionGenerationStep().process(context);

        final Content bookProjectionActor =
                context.findContent(PROJECTION, "BookProjectionActor");

        final Content authorProjectionActor =
                context.findContent(PROJECTION, "AuthorProjectionActor");

        final Content dispatcherProvider =
                context.findContent(PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

        Assertions.assertTrue(authorProjectionActor.contains(TextExpectation.onJava().read("event-based-author-projection-actor-for-sourced-entities")));
        Assertions.assertTrue(bookProjectionActor.contains(TextExpectation.onJava().read("event-based-book-projection-actor-for-sourced-entities")));
        Assertions.assertTrue(dispatcherProvider.contains(TextExpectation.onJava().read("event-based-projection-dispatcher-provider")));
    }

    @Test
    public void testThatEventBasedProjectionClassesAreGeneratedForStatefulEntities() throws IOException {
        final CodeGenerationContext context =
                CodeGenerationContext.with(codeGenerationParameters());

        loadParameters(context, StorageType.STATE_STORE.name(), ProjectionType.EVENT_BASED.name());
        loadContents(context);
        new ProjectionGenerationStep().process(context);

        final Content bookProjectionActor =
                context.findContent(PROJECTION, "BookProjectionActor");

        final Content authorProjectionActor =
                context.findContent(PROJECTION, "AuthorProjectionActor");

        final Content dispatcherProvider =
                context.findContent(PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

        Assertions.assertTrue(authorProjectionActor.contains(TextExpectation.onJava().read("event-based-author-projection-actor-for-stateful-entities")));
        Assertions.assertTrue(bookProjectionActor.contains(TextExpectation.onJava().read("event-based-book-projection-actor-for-stateful-entities")));
        Assertions.assertTrue(dispatcherProvider.contains(TextExpectation.onJava().read("event-based-projection-dispatcher-provider")));
    }

    @Test
    public void testThatOperationBasedProjectionClassesAreGenerated() throws IOException {
        final CodeGenerationContext context =
                CodeGenerationContext.with(codeGenerationParameters());

        loadParameters(context, StorageType.STATE_STORE.name(), ProjectionType.OPERATION_BASED.name());
        loadContents(context);
        new ProjectionGenerationStep().process(context);

        final Content bookProjectionActor =
                context.findContent(PROJECTION, "BookProjectionActor");

        final Content authorProjectionActor =
                context.findContent(PROJECTION, "AuthorProjectionActor");

        final Content dispatcherProvider =
                context.findContent(PROJECTION_DISPATCHER_PROVIDER, "ProjectionDispatcherProvider");

        Assertions.assertTrue(authorProjectionActor.contains(TextExpectation.onJava().read("operation-based-author-projection-actor")));
        Assertions.assertTrue(bookProjectionActor.contains(TextExpectation.onJava().read("operation-based-book-projection-actor")));
        Assertions.assertTrue(dispatcherProvider.contains(TextExpectation.onJava().read("operation-based-projection-dispatcher-provider")));
    }

    private void loadContents(final CodeGenerationContext context) {
        context.addContent(AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.java"), AUTHOR_STATE_CONTENT_TEXT);
        context.addContent(AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.java"), BOOK_STATE_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), AUTHOR_CONTENT_TEXT);
        context.addContent(DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRegistered.java"), AUTHOR_REGISTERED_CONTENT_TEXT);
        context.addContent(DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRanked.java"), AUTHOR_RANKED_CONTENT_TEXT);
        context.addContent(AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "Book.java"), BOOK_CONTENT_TEXT);
        context.addContent(DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookCataloged.java"), BOOK_CATALOGED_CONTENT_TEXT);
        context.addContent(DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.java"), AUTHOR_DATA_CONTENT_TEXT);
        context.addContent(DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.java"), BOOK_DATA_CONTENT_TEXT);
    }

    private void loadParameters(final CodeGenerationContext context, final String storage, final String projections) {
        context.with(PACKAGE, "io.vlingo").with(APPLICATION_NAME, "xoomapp")
                .with(STORAGE_TYPE, storage).with(PROJECTION_TYPE, projections)
                .with(TARGET_FOLDER, HOME_DIRECTORY);
    }

    private CodeGenerationParameters aggregates() {
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
                        .relate(FACTORY_METHOD, "true")
                        .relate(authorRegisteredEvent);

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank")
                        .relate(authorRankedEvent);

        final CodeGenerationParameter authorAggregate =
                CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                        .relate(URI_ROOT, "/authors").relate(idField)
                        .relate(nameField).relate(rankField).relate(factoryMethod)
                        .relate(rankMethod).relate(authorRegisteredEvent).relate(authorRankedEvent);

        final CodeGenerationParameter titleField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "title")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter publisherField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "publisher")
                        .relate(Label.FIELD_TYPE, "String");

        final CodeGenerationParameter bookCatalogedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "BookCataloged")
                        .relate(idField).relate(titleField).relate(publisherField);

        final CodeGenerationParameter catalogMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "catalog")
                        .relate(Label.METHOD_PARAMETER, "title")
                        .relate(Label.METHOD_PARAMETER, "publisher")
                        .relate(FACTORY_METHOD, "true")
                        .relate(bookCatalogedEvent);

        final CodeGenerationParameter bookAggregate =
                CodeGenerationParameter.of(Label.AGGREGATE, "Book")
                        .relate(idField).relate(titleField)
                        .relate(publisherField).relate(catalogMethod)
                        .relate(bookCatalogedEvent);

        return CodeGenerationParameters.from(authorAggregate, bookAggregate);
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

    private CodeGenerationParameters codeGenerationParameters() {
        return aggregates().add(nameValueObject()).add(rankValueObject())
                .add(classificationValueObject()).add(classifierValueObject());
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

    private static final String BOOK_CATALOGED_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.book; \\n" +
                    "public class BookCataloged extends DomainEvent { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_REGISTERED_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorRegistered extends DomainEvent { \\n" +
                    "... \\n" +
                    "}";

    private static final String AUTHOR_RANKED_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model.author; \\n" +
                    "public class AuthorRanked extends DomainEvent { \\n" +
                    "... \\n" +
                    "}";

    private static final String  AUTHOR_DATA_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure; \\n" +
                    "public class AuthorData { \\n" +
                    "... \\n" +
                    "}";

    private static final String BOOK_DATA_CONTENT_TEXT =
            "package io.vlingo.xoomapp.infrastructure; \\n" +
                    "public class BookData { \\n" +
                    "... \\n" +
                    "}";
}
