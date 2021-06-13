package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectionUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final List<CodeGenerationParameter> aggregates =
        context.parametersOf(Label.AGGREGATE).collect(Collectors.toList());

    final List<CodeGenerationParameter> valueObjects =
        context.parametersOf(Label.VALUE_OBJECT).collect(Collectors.toList());

    return ProjectionUnitTestTemplateData.from(context.contents(), aggregates, valueObjects);
  }
}
