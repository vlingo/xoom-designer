package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateProcessingStep;

import java.util.List;

public class RestUiResourceGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    return RestUiResourceTemplateDataFactory.build(context.parameters(), context.contents());
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return true;
  }

}
