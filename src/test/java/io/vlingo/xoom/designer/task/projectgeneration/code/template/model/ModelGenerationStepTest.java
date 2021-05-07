package io.vlingo.xoom.designer.task.projectgeneration.code.template.model;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.TextExpectation;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.turbo.codegen.template.OutputFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.COLLECTION_TYPE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.PACKAGE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType.EVENT_BASED;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType.OPERATION_BASED;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType.JOURNAL;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType.STATE_STORE;

public class ModelGenerationStepTest {

    @Test
    public void testThatEventBasedStatefulModelIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, STATE_STORE),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, EVENT_BASED),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.JAVA),
                        CodeGenerationParameter.of(Label.CQRS, true),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content authorProtocol = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(authorProtocol.contains(TextExpectation.onJava().read("author-protocol")));
        Assertions.assertTrue(authorEntity.contains(TextExpectation.onJava().read("event-based-stateful-author-entity")));
        Assertions.assertTrue(authorState.contains(TextExpectation.onJava().read("stateful-author-state")));
        Assertions.assertTrue(authorRegistered.contains(TextExpectation.onJava().read("author-registered")));
        Assertions.assertTrue(authorRanked.contains(TextExpectation.onJava().read("author-ranked")));
    }

    @Test
    public void testThatOperationBasedStatefulModelIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, STATE_STORE),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, OPERATION_BASED),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.JAVA),
                        CodeGenerationParameter.of(Label.CQRS, true),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content authorProtocol = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(authorProtocol.contains(TextExpectation.onJava().read("author-protocol")));
        Assertions.assertTrue(authorEntity.contains(TextExpectation.onJava().read("operation-based-stateful-author-entity")));
        Assertions.assertTrue(authorState.contains(TextExpectation.onJava().read("stateful-author-state")));
        Assertions.assertTrue(authorRegistered.contains(TextExpectation.onJava().read("author-registered")));
        Assertions.assertTrue(authorRanked.contains(TextExpectation.onJava().read("author-ranked")));
    }

    @Test
    public void testThatOperationBasedStatefulSingleModelIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, STATE_STORE),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.NONE),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.JAVA),
                        CodeGenerationParameter.of(Label.CQRS, false),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content authorProtocol = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(authorProtocol.contains(TextExpectation.onJava().read("author-protocol-for-single-model")));
        Assertions.assertTrue(authorEntity.contains(TextExpectation.onJava().read("operation-based-stateful-single-model-author-entity")));
        Assertions.assertTrue(authorState.contains(TextExpectation.onJava().read("stateful-author-state")));
        Assertions.assertTrue(authorRegistered.contains(TextExpectation.onJava().read("author-registered")));
        Assertions.assertTrue(authorRanked.contains(TextExpectation.onJava().read("author-ranked")));
    }

    @Test
    public void testThatSourcedModelIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, JOURNAL),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, EVENT_BASED),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.JAVA),
                        CodeGenerationParameter.of(Label.CQRS, true),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content authorProtocol = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(authorProtocol.contains(TextExpectation.onJava().read("author-protocol")));
        Assertions.assertTrue(authorEntity.contains(TextExpectation.onJava().read("sourced-author-entity")));
        Assertions.assertTrue(authorState.contains(TextExpectation.onJava().read("sourced-author-state")));
        Assertions.assertTrue(authorRegistered.contains(TextExpectation.onJava().read("author-registered")));
        Assertions.assertTrue(authorRanked.contains(TextExpectation.onJava().read("author-ranked")));
    }

    @Test
    public void testThatSourcedSingleModelIsGenerated() throws IOException {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, JOURNAL),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, EVENT_BASED),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.JAVA),
                        CodeGenerationParameter.of(Label.CQRS, false),
                        authorAggregate(), nameValueObject(), rankValueObject());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content authorProtocol = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(authorProtocol.contains(TextExpectation.onJava().read("author-protocol-for-single-model")));
        Assertions.assertTrue(authorEntity.contains(TextExpectation.onJava().read("sourced-single-model-author-entity")));
        Assertions.assertTrue(authorState.contains(TextExpectation.onJava().read("sourced-author-state")));
        Assertions.assertTrue(authorRegistered.contains(TextExpectation.onJava().read("author-registered")));
        Assertions.assertTrue(authorRanked.contains(TextExpectation.onJava().read("author-ranked")));
    }

    @Test
    @Disabled
    public void testThatStatefulModelIsGeneratedOnKotlin() {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(CodeGenerationParameter.of(PACKAGE, "io.vlingo.xoomapp"),
                        CodeGenerationParameter.of(Label.STORAGE_TYPE, STATE_STORE),
                        CodeGenerationParameter.of(Label.PROJECTION_TYPE, EVENT_BASED),
                        CodeGenerationParameter.of(Label.LANGUAGE, Language.KOTLIN),
                        authorAggregate());

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters).contents(contents());

        final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

        Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

        modelGenerationStep.process(context);

        final Content author = context.findContent(AGGREGATE_PROTOCOL, "Author");
        final Content authorEntity = context.findContent(AGGREGATE, "AuthorEntity");
        final Content authorState = context.findContent(AGGREGATE_STATE, "AuthorState");
        final Content authorRegistered = context.findContent(DOMAIN_EVENT, "AuthorRegistered");
        final Content authorRanked = context.findContent(DOMAIN_EVENT, "AuthorRanked");

        Assertions.assertEquals(7, context.contents().size());
        Assertions.assertTrue(author.contains("interface Author "));
        Assertions.assertTrue(author.contains("val _address = stage.addressFactory().uniquePrefixedWith(\"g-\") : Address"));
        Assertions.assertTrue(author.contains("val _author = stage.actorFor(Author::class.java, Definition.has(AuthorEntity::class.java, Definition.parameters(_address.idString())), _address) : Author"));
        Assertions.assertTrue(author.contains("return _author.withName(name)"));
        Assertions.assertTrue(authorEntity.contains("public class AuthorEntity : StatefulEntity<AuthorState>, Author"));
        Assertions.assertTrue(authorEntity.contains("public fun withName(final Name name): Completes<AuthorState>"));
        Assertions.assertTrue(authorEntity.contains("val stateArg: AuthorState = state.withName(name)"));
        Assertions.assertTrue(authorEntity.contains("return apply(stateArg, AuthorRegistered(stateArg)){state}"));
        Assertions.assertTrue(authorState.contains("class AuthorState"));
        Assertions.assertTrue(authorState.contains("val id: String;"));
        Assertions.assertTrue(authorState.contains("val name: Name;"));
        Assertions.assertTrue(authorState.contains("val rank: Rank;"));
        Assertions.assertTrue(authorRegistered.contains("class AuthorRegistered : IdentifiedDomainEvent"));
        Assertions.assertTrue(authorRegistered.contains("val id: String;"));
        Assertions.assertTrue(authorRegistered.contains("val name: Name;"));
        Assertions.assertTrue(authorRanked.contains("class AuthorRanked : IdentifiedDomainEvent"));
        Assertions.assertTrue(authorRanked.contains("val id: String;"));
        Assertions.assertTrue(authorRanked.contains("val rank: Rank;"));
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

        final CodeGenerationParameter availableOnField =
                CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn")
                        .relate(Label.FIELD_TYPE, "LocalDate");

        final CodeGenerationParameter relatedAuthors =
                CodeGenerationParameter.of(Label.STATE_FIELD, "relatedAuthors")
                        .relate(Label.FIELD_TYPE, "String")
                        .relate(Label.COLLECTION_TYPE, "Set");

        final CodeGenerationParameter authorRegisteredEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "name"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "availableOn"));

        final CodeGenerationParameter authorRankedEvent =
                CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
                        .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "rank"));

        final CodeGenerationParameter factoryMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
                        .relate(Label.METHOD_PARAMETER, "name")
                        .relate(Label.FACTORY_METHOD, "true")
                        .relate(authorRegisteredEvent);

        final CodeGenerationParameter rankMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "changeRank")
                        .relate(Label.METHOD_PARAMETER, "rank")
                        .relate(authorRankedEvent);

        final CodeGenerationParameter hideMethod =
                CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "hide");

        return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
                .relate(idField).relate(nameField).relate(rankField).relate(relatedAuthors)
                .relate(availableOnField).relate(factoryMethod).relate(rankMethod).relate(hideMethod)
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
                                .relate(Label.FIELD_TYPE, "String").relate(COLLECTION_TYPE, "Set"));
    }

    private Content[] contents() {
        return new Content[] {
                Content.with(VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Rank.java"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT),
                Content.with(VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT)
        };
    }

    private static final String PROJECT_PATH =
            OperatingSystem.detect().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String MODEL_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java",
                    "io", "vlingo", "xoomapp", "model").toString();

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

}
