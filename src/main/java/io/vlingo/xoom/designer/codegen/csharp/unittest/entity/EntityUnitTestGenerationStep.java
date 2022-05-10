package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUnitTestGenerationStep  extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);

    final List<CodeGenerationParameter> aggregates = context.parametersOf(Label.AGGREGATE)
        .collect(Collectors.toList());

    final List<Content> contents = context.contents();

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new MockDispatcherTemplateData(basePackage));
    templatesData.addAll(EntityUnitTestTemplateData.from(basePackage, aggregates, contents));
    return templatesData;
  }

  @Override
  protected Dialect resolveDialect(CodeGenerationContext context) {
    final String dialectName = dialectNameFrom(context);
    return dialectName.isEmpty() ? super.resolveDialect(context) : Dialect.withName(dialectName);
  }

  private String dialectNameFrom(CodeGenerationContext context) {
    return context.parameterOf(Label.DIALECT);
  }

}
