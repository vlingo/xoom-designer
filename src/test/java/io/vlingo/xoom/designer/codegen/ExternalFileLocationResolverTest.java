package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.ModelTemplateDataFactory;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;
import io.vlingo.xoom.designer.codegen.java.unittest.entity.EntityUnitTestTemplateData;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalFileLocationResolverTest extends CodeGenerationTest{

  private final String separator = File.separator;

  @Test
  public void testThatProductionCodeRelativeSourcePathForJavaIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContextFactory.build(Logger.noOpLogger(), parameters);

    final TemplateData templateData = ModelTemplateDataFactory.from(context)
        .stream().filter(template -> template.hasStandard(JavaTemplateStandard.AGGREGATE)).findFirst().get();

    String result = context.fileLocationResolver().resolve(context, Dialect.JAVA, templateData);

    assertEquals("src" + separator + "main" + separator + "java" + separator + "io" + separator + "vlingo" + separator + "xoomapp" + separator + "model" + separator + "author", result);
  }

  @Test
  public void testThatUnitTestCodeRelativeSourcePathForJavaIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "io.vlingo.xoomapp")
        .add(Label.DIALECT, Dialect.JAVA)
        .add(Label.STORAGE_TYPE, StorageType.JOURNAL)
        .add(Label.PROJECTION_TYPE, ProjectionType.EVENT_BASED)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContextFactory.build(Logger.noOpLogger(), parameters);

    final List<TemplateData> from = EntityUnitTestTemplateData.from("io.vlingo.xoomapp", StorageType.JOURNAL, ProjectionType.EVENT_BASED, Collections.singletonList(authorAggregate()), Collections.emptyList(), Arrays.asList(contents()));
    final TemplateData templateData = from
        .stream().filter(template -> template.hasStandard(JavaTemplateStandard.ENTITY_UNIT_TEST)).findFirst().get();

    String result = context.fileLocationResolver().resolve(context, Dialect.JAVA, templateData);

    assertEquals("src" + separator + "test" + separator + "java" + separator + "io" + separator + "vlingo" + separator + "xoomapp" + separator + "model" + separator + "author", result);
  }


  @Test
  public void testThatProductionCodeRelativeSourcePathForCsharpIsGenerated() {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));

    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContextFactory.build(Logger.noOpLogger(), parameters);

    final TemplateData templateData = io.vlingo.xoom.designer.codegen.csharp.model.ModelTemplateDataFactory.from(context)
        .stream().filter(template -> template.hasStandard(CsharpTemplateStandard.AGGREGATE)).findFirst().get();

    String result = context.fileLocationResolver().resolve(context, Dialect.C_SHARP, templateData);

    assertEquals("Io.Vlingo.Xoomapp" + separator + "Model" + separator + "Author", result);
  }


  @Test
  public void testThatUnitTestCodeRelativeSourcePathForCsharpIsGenerated() {
    ComponentRegistry.register("cSharpCodeFormatter", CodeElementFormatter.with(Dialect.C_SHARP, ReservedWordsHandler.usingSuffix("_")));

    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.DIALECT, Dialect.C_SHARP)
        .add(authorAggregate());

    final CodeGenerationContext context = CodeGenerationContextFactory.build(Logger.noOpLogger(), parameters);

    final List<TemplateData> from = io.vlingo.xoom.designer.codegen.csharp.unittest.entity.EntityUnitTestTemplateData.from("Io.Vlingo.Xoomapp", Collections.singletonList(authorAggregate()), Arrays.asList(cSharpContents()));
    final TemplateData templateData = from
        .stream().filter(template -> template.hasStandard(CsharpTemplateStandard.ENTITY_UNIT_TEST)).findFirst().get();

    String result = context.fileLocationResolver().resolve(context, Dialect.C_SHARP, templateData);

    assertEquals("Io.Vlingo.Xoomapp.Tests" + separator + "Model" + separator + "Author", result);
  }

  private CodeGenerationParameter authorAggregate() {
    final CodeGenerationParameter idField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "id")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "name")
            .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter rankField =
        CodeGenerationParameter.of(Label.STATE_FIELD, "rank")
            .relate(Label.FIELD_TYPE, "Double")
            .relate(Label.COLLECTION_TYPE, "List");

    final CodeGenerationParameter authorRegisteredEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRegistered")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"));

    final CodeGenerationParameter authorRankedEvent =
        CodeGenerationParameter.of(Label.DOMAIN_EVENT, "AuthorRanked")
            .relate(CodeGenerationParameter.of(Label.STATE_FIELD, "id"))
            .relate(rankField);

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
        .relate(idField).relate(nameField).relate(rankField)
        .relate(factoryMethod).relate(rankMethod).relate(hideMethod)
        .relate(authorRegisteredEvent).relate(authorRankedEvent);
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "Author.java"), null, null, AUTHOR_CONTENT_TEXT),
    };
  }

  private Content[] cSharpContents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(separator + "Projects" + separator + "", "IAuthor.cs"), null, null, C_SHARP_AUTHOR_CONTENT_TEXT),
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
  private static final String C_SHARP_AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";
}
