package io.vlingo.xoom.designer.codegen.java.unittest.resource;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RestResourceUnitTestTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationParameters parameters, final List<Content> contents,
                                         List<CodeGenerationParameter> valueObjects) {
    final String basePackage = parameters.retrieveValue(Label.PACKAGE);
    final Function<CodeGenerationParameter, TemplateData> mapper =
        aggregateParameter -> new RestResourceUnitTestTemplateData(basePackage, aggregateParameter, contents, valueObjects);

    return parameters.retrieveAll(Label.AGGREGATE)
        .filter(aggregateParameter -> aggregateParameter.hasAny(Label.ROUTE_SIGNATURE))
        .map(mapper).collect(Collectors.toList());
  }

  public static TemplateData buildAbstract(final CodeGenerationParameters parameters, final List<Content> contents) {
    return new RestResourceAbstractUnitTestTemplateData(parameters.retrieveValue(Label.PACKAGE),
        parameters.retrieveAll(Label.AGGREGATE).findFirst().orElseThrow(IllegalArgumentException::new),
        contents);
  }

}
