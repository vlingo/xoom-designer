package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class RestResourceAbstractUnitTestTemplateData extends TemplateData {
  private final String packageName;
  private final TemplateParameters parameters;

  public RestResourceAbstractUnitTestTemplateData(String basePackage, CodeGenerationParameter aggregateParameter,
                                                  List<Content> contents) {
    this.packageName = resolvePackage(basePackage);
    this.parameters = loadParameters(aggregateParameter, contents);
  }

  private TemplateParameters loadParameters(final CodeGenerationParameter aggregate, final List<Content> contents) {
    final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

    return TemplateParameters.with(PACKAGE_NAME, packageName)
        .addImport(resolveImports(dataObjectName, contents))
        .and(PRODUCTION_CODE, false)
        .and(UNIT_TEST, true);
  }

  private String resolveImports(final String dataObjectName, final List<Content> contents) {
    final String dataObjectPackage =
        ContentQuery.findPackage(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents);

    final CodeElementFormatter codeElementFormatter =
            ComponentRegistry.withName("defaultCodeFormatter");

    return codeElementFormatter.importAllFrom(dataObjectPackage);
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "infrastructure", "resource");
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.ABSTRACT_REST_RESOURCE_UNIT_TEST;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public String filename() {
    return "AbstractRestTest";
  }
}
