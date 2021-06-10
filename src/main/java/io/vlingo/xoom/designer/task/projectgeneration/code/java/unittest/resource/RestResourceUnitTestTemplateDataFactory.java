package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RestResourceUnitTestTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationParameters parameters, final List<Content> contents) {
    final String basePackage = parameters.retrieveValue(Label.PACKAGE);

    final Function<CodeGenerationParameter, TemplateData> mapper =
        aggregateParameter -> new RestResourceUnitTestTemplateData(basePackage, aggregateParameter, contents);

    return parameters.retrieveAll(Label.AGGREGATE)
        .filter(aggregateParameter -> aggregateParameter.hasAny(Label.ROUTE_SIGNATURE))
        .map(mapper).collect(Collectors.toList());
  }

}
