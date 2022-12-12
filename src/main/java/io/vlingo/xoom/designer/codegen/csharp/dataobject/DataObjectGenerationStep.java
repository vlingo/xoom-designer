package io.vlingo.xoom.designer.codegen.csharp.dataobject;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DataObjectGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final List<CodeGenerationParameter> valueObjects = context.parametersOf(Label.VALUE_OBJECT).collect(toList());

    final List<TemplateData> stateDataObjectTemplateData = StateDataObjectTemplateData
        .from(context.parameterOf(Label.PACKAGE), context.parameterOf(Label.DIALECT, Dialect::valueOf),
            context.parametersOf(Label.AGGREGATE), valueObjects, context.contents());

    final List<TemplateData> valueDataObjectTemplateData = ValueDataObjectTemplateData
        .from(context.parameterOf(Label.PACKAGE), context.parameterOf(Label.DIALECT, Dialect::valueOf),
            valueObjects, context.contents());

    return Stream.of(stateDataObjectTemplateData, valueDataObjectTemplateData)
        .flatMap(List::stream)
        .collect(toList());
  }

  @Override
  public boolean shouldProcess(CodeGenerationContext context) {
    return resolveDialect(context).equals(Dialect.C_SHARP);
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
