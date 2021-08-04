package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.projections;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String packageName =
        ContentQuery.findPackage(JavaTemplateStandard.PERSISTENCE_SETUP, context.contents());
    final List<CodeGenerationParameter> aggregates =
        context.parametersOf(Label.AGGREGATE).collect(Collectors.toList());

    final ProjectionType projectionType =
        context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final List<CodeGenerationParameter> valueObjects =
        context.parametersOf(Label.VALUE_OBJECT).collect(Collectors.toList());
    final List<TemplateData> templatesData = new ArrayList<>();
    if (projectionType.isEventBased()) {
      templatesData.add(new CountingProjectionControlTemplateData(packageName));
      templatesData.add(new CountingReadResultInterestTemplateData(packageName));
    }
    templatesData.addAll(ProjectionUnitTestTemplateData.from(context.contents(), packageName, projectionType, aggregates, valueObjects));
    return templatesData;

  }

  @Override
  public boolean shouldProcess(CodeGenerationContext context) {
    return context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf).isProjectionEnabled();
  }

}
