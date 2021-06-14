package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class ProjectionUnitTestGenerationStepTest {

  @Test
  public void testThatProjectionsUnitTestAreGenerated() throws IOException {
    // GIVEN
    final CodeGenerationParameters parameters = codeGenerationParameters();
    final CodeGenerationContext context =
        CodeGenerationContext.with(parameters).contents(contents());

    // WHEN
    new ProjectionUnitTestGenerationStep().process(context);

    // THEN
    final Content authorProjectionTest =
        context.findContent(JavaTemplateStandard.PROJECTION_UNIT_TEST, "AuthorProjectionTests");

    Assertions.assertEquals(5, context.contents().size());
    Assertions.assertTrue(authorProjectionTest.contains(TextExpectation.onJava().read("author-projection-unit-test")));
  }

  private CodeGenerationParameters codeGenerationParameters() {
    return CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(authorAggregate())
        .add(nameValueObject()).add(rankValueObject())
        .add(classificationValueObject()).add(classifierValueObject());
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
            .relate(Label.FIELD_TYPE, "LocalDate");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Author")
        .relate(idField).relate(nameField).relate(rankField).relate(statusField).relate(availableOnField);
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
        Content.with(JavaTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorData.java"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRegistered.java"), null, null, AUTHOR_REGISTERED_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRanked.java"), null, null, AUTHOR_RANKED_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.PROJECTION, new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "AuthorProjectionActor.java"), null, null, AUTHOR_PROJECTION_CONTENT_TEXT),

    };
  }

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "src", "main", "java",
          "io", "vlingo", "xoomapp", "model").toString();
  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure; \\n" +
          "public class AuthorData { \\n" +
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

  private static final String AUTHOR_PROJECTION_CONTENT_TEXT =
      "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
          "public class AuthorProjectionActor { \\n" +
          "... \\n" +
          "}";
}
