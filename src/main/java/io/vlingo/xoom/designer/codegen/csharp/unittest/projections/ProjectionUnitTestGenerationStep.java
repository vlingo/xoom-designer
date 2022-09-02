package io.vlingo.xoom.designer.codegen.csharp.unittest.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final List<CodeGenerationParameter> aggregates =
        context.parametersOf(Label.AGGREGATE).collect(Collectors.toList());
    final ProjectionType projectionType =
        context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final List<CodeGenerationParameter> valueObjects =
        context.parametersOf(Label.VALUE_OBJECT).collect(Collectors.toList());

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new CountingProjectionControlTemplateData(basePackage));
    templatesData.add(new CountingReadResultInterestTemplateData(basePackage));
    templatesData.addAll(ProjectionUnitTestTemplateData.from(context.contents(), basePackage, projectionType, aggregates, valueObjects));

    return templatesData;
  }

  @Override
  public boolean shouldProcess(CodeGenerationContext context) {
    final Dialect dialect = resolveDialect(context);
    return dialect.equals(Dialect.C_SHARP) && context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf)
        .isProjectionEnabled();
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
