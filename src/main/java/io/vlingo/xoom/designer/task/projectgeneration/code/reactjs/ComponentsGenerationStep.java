// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.BasicTemplateData;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.resource.RouteDetail;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.template.ParameterKey.Defaults.PACKAGE_NAME;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.VALUE_OBJECT_FIELD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.TemplateParameter.AGGREGATES;
import static java.util.stream.Collectors.toList;

public class ComponentsGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String artifactId =
            context.parameterOf(Label.ARTIFACT_ID);

    final Map<String, List<Field>> valueObjectTypes =
            indexValueObjects(context.parametersOf(Label.VALUE_OBJECT));

    final List<Aggregate> aggregates =
            context.parametersOf(AGGREGATE).filter(RouteDetail::requireModelFactory)
                    .map(Aggregate::new).collect(toList());

    final List<TemplateData> layoutTemplates =
            Arrays.asList(headerComponentTemplateData(artifactId), appComponentTemplateData(aggregates),
                    sidebarComponentTemplateData(aggregates));

    return Stream.of(layoutTemplates, aggregateTemplates(aggregates, valueObjectTypes))
            .flatMap(List::stream).collect(toList());
  }

  private TemplateData headerComponentTemplateData(final String artifactId) {
    return BasicTemplateData.of(ReactJsTemplateStandard.HEADER,
            TemplateParameters.with(PACKAGE_NAME, "src.components").and(TemplateParameter.ARTIFACT_ID, artifactId));
  }

  private TemplateData appComponentTemplateData(final List<Aggregate> aggregates) {
    return BasicTemplateData.of(ReactJsTemplateStandard.APP,
            TemplateParameters.with(PACKAGE_NAME, "src").and(AGGREGATES, aggregates));
  }

  private TemplateData sidebarComponentTemplateData(final List<Aggregate> aggregates) {
    return BasicTemplateData.of(ReactJsTemplateStandard.SIDEBAR,
            TemplateParameters.with(PACKAGE_NAME, "src.components").and(AGGREGATES, aggregates));
  }

  private List<TemplateData> aggregateTemplates(final List<Aggregate> aggregates,
                                                final Map<String, List<Field>> valueObjectTypes) {
    final List<TemplateData> aggregateListTemplates =
            AggregateListTemplateData.from(aggregates, valueObjectTypes);

    final List<TemplateData> aggregateDetailTemplates =
            AggregateDetailTemplateData.from(aggregates, valueObjectTypes);

    final List<TemplateData> aggregateMethodTemplateData =
            AggregateMethodTemplateData.from(aggregates, valueObjectTypes);

    return Stream.of(aggregateListTemplates, aggregateDetailTemplates, aggregateMethodTemplateData)
            .flatMap(List::stream).collect(toList());
  }

  private Map<String, List<Field>> indexValueObjects(final Stream<CodeGenerationParameter> valueObjects) {
    return valueObjects.collect(Collectors.toMap(vo -> vo.value,
            vo -> vo.retrieveAllRelated(VALUE_OBJECT_FIELD).map(Field::new).collect(toList()),
            (a, b) -> a, LinkedHashMap::new));
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.parametersOf(AGGREGATE).anyMatch(RouteDetail::requireModelFactory);
  }

  @Override
  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.REACTJS;
  }

}
