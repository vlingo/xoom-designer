// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.Aggregate;
import io.vlingo.xoom.designer.codegen.java.Field;
import io.vlingo.xoom.designer.codegen.java.resource.RouteDetail;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateTemplatesDataFactory {

  public static List<TemplateData> from(final CodeGenerationContext context) {
    final Map<String, List<Field>> valueObjectTypes =
            indexValueObjects(context.parametersOf(Label.VALUE_OBJECT));

    final List<Aggregate> aggregates =
            context.parametersOf(Label.AGGREGATE).filter(RouteDetail::requireModelFactory)
                    .map(Aggregate::new).collect(toList());

    final List<TemplateData> aggregateListTemplates =
            AggregateListTemplateData.from(aggregates, valueObjectTypes);

    final List<TemplateData> aggregateDetailTemplates =
            AggregateDetailTemplateData.from(aggregates, valueObjectTypes);

    final List<TemplateData> aggregateMethodTemplateData =
            AggregateMethodTemplateData.from(aggregates, valueObjectTypes);

    return Stream.of(aggregateListTemplates, aggregateDetailTemplates, aggregateMethodTemplateData)
            .flatMap(List::stream).collect(toList());
  }

  private static Map<String, List<Field>> indexValueObjects(final Stream<CodeGenerationParameter> valueObjects) {
    return valueObjects.collect(Collectors.toMap(vo -> vo.value,
            vo -> vo.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).map(Field::new).collect(toList()),
            (a, b) -> a, LinkedHashMap::new));
  }
}
