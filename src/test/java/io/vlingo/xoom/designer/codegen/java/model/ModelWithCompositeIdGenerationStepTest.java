package io.vlingo.xoom.designer.codegen.java.model;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ModelWithCompositeIdGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatEventBasedStatefulIsGenerated()  {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA),
            CodeGenerationParameter.of(Label.CQRS, true),
            catalogAggregate(), nameValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content catalogProtocol = context.findContent(JavaTemplateStandard.AGGREGATE_PROTOCOL, "Catalog");
    final Content catalogEntity = context.findContent(JavaTemplateStandard.AGGREGATE, "CatalogEntity");
    final Content catalogState = context.findContent(JavaTemplateStandard.AGGREGATE_STATE, "CatalogState");
    final Content catalogCreated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogCreated");
    final Content catalogUpdated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogUpdated");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(catalogProtocol.contains(TextExpectation.onJava().read("catalog-protocol-with-composite-id")));
    Assertions.assertTrue(catalogEntity.contains(TextExpectation.onJava().read("event-based-stateful-catalog-entity")));
    Assertions.assertTrue(catalogState.contains(TextExpectation.onJava().read("stateful-catalog-state")));
    Assertions.assertTrue(catalogCreated.contains(TextExpectation.onJava().read("catalog-created")));
    Assertions.assertTrue(catalogUpdated.contains(TextExpectation.onJava().read("catalog-updated")));
  }

  @Test
  public void testThatOperationBasedStatefulModelIsGenerated()  {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA),
            CodeGenerationParameter.of(Label.CQRS, true),
            catalogAggregate(), nameValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content catalogProtocol = context.findContent(JavaTemplateStandard.AGGREGATE_PROTOCOL, "Catalog");
    final Content catalogEntity = context.findContent(JavaTemplateStandard.AGGREGATE, "CatalogEntity");
    final Content catalogState = context.findContent(JavaTemplateStandard.AGGREGATE_STATE, "CatalogState");
    final Content catalogCreated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogCreated");
    final Content catalogUpdated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogUpdated");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(catalogProtocol.contains(TextExpectation.onJava().read("catalog-protocol-with-composite-id")));
    Assertions.assertTrue(catalogEntity.contains(TextExpectation.onJava().read("operation-based-stateful-catalog-entity")));
    Assertions.assertTrue(catalogState.contains(TextExpectation.onJava().read("stateful-catalog-state")));
    Assertions.assertTrue(catalogCreated.contains(TextExpectation.onJava().read("catalog-created")));
    Assertions.assertTrue(catalogUpdated.contains(TextExpectation.onJava().read("catalog-updated")));
  }

  @Test
  public void testThatOperationBasedStatefulSingleModelIsGenerated()  {
    final CodeGenerationParameters parameters =
        CodeGenerationParameters.from(CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp"),
            CodeGenerationParameter.of(Label.STORAGE_TYPE, StorageType.STATE_STORE),
            CodeGenerationParameter.of(Label.PROJECTION_TYPE, ProjectionType.NONE),
            CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA),
            CodeGenerationParameter.of(Label.CQRS, false),
            catalogAggregate(), nameValueObject());

    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    final ModelGenerationStep modelGenerationStep = new ModelGenerationStep();

    Assertions.assertTrue(modelGenerationStep.shouldProcess(context));

    modelGenerationStep.process(context);

    final Content catalogProtocol = context.findContent(JavaTemplateStandard.AGGREGATE_PROTOCOL, "Catalog");
    final Content catalogEntity = context.findContent(JavaTemplateStandard.AGGREGATE, "CatalogEntity");
    final Content catalogState = context.findContent(JavaTemplateStandard.AGGREGATE_STATE, "CatalogState");
    final Content catalogCreated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogCreated");
    final Content catalogUpdated = context.findContent(JavaTemplateStandard.DOMAIN_EVENT, "CatalogUpdated");

    Assertions.assertEquals(6, context.contents().size());
    Assertions.assertTrue(catalogProtocol.contains(TextExpectation.onJava().read("catalog-protocol-for-single-model-with-composite-id")));
    Assertions.assertTrue(catalogEntity.contains(TextExpectation.onJava().read("operation-based-stateful-single-model-catalog-entity")));
    Assertions.assertTrue(catalogState.contains(TextExpectation.onJava().read("stateful-catalog-state")));
    Assertions.assertTrue(catalogCreated.contains(TextExpectation.onJava().read("catalog-created")));
    Assertions.assertTrue(catalogUpdated.contains(TextExpectation.onJava().read("catalog-updated")));
  }

  private CodeGenerationParameter catalogAggregate() {
    final CodeGenerationParameter idField = CodeGenerationParameter.of(Label.STATE_FIELD, "id")
        .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter authorIdField = CodeGenerationParameter.of(Label.STATE_FIELD, "authorId")
        .relate(Label.FIELD_TYPE, "CompositeId");

    final CodeGenerationParameter nameField = CodeGenerationParameter.of(Label.STATE_FIELD, "name")
        .relate(Label.FIELD_TYPE, "Name");


    final CodeGenerationParameter catalogCreatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "CatalogCreated")
        .relate(idField).relate(authorIdField).relate(nameField);

    final CodeGenerationParameter catalogUpdatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "CatalogUpdated")
        .relate(idField).relate(authorIdField).relate(nameField);

    final CodeGenerationParameter factoryMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
        .relate(Label.METHOD_PARAMETER, "authorId")
        .relate(Label.METHOD_PARAMETER, "name")
        .relate(Label.FACTORY_METHOD, "true")
        .relate(catalogCreatedEvent);

    final CodeGenerationParameter updateMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "update")
        .relate(Label.METHOD_PARAMETER, "id")
        .relate(Label.METHOD_PARAMETER, "authorId")
        .relate(Label.METHOD_PARAMETER, "name")
        .relate(catalogCreatedEvent);

    return CodeGenerationParameter.of(Label.AGGREGATE, "Catalog")
        .relate(idField).relate(authorIdField).relate(nameField)
        .relate(factoryMethod).relate(updateMethod)
        .relate(catalogCreatedEvent).relate(catalogUpdatedEvent);
  }

  private CodeGenerationParameter nameValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
            .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
                    .relate(Label.FIELD_TYPE, "String"))
            .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
                    .relate(Label.FIELD_TYPE, "String"));
  }

  private Content[] contents() {
    return new Content[]{
            Content.with(JavaTemplateStandard.VALUE_OBJECT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Name.java"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT),
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

}
