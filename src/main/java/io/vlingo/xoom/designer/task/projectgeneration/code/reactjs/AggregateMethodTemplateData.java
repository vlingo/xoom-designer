// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.Aggregate;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.AggregateMethod;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.Field;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.TemplateParameter.*;

public class AggregateMethodTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public static List<TemplateData> from(final List<Aggregate> aggregates,
                                        final Map<String, List<Field>> valueObjectTypes) {
    return aggregates.stream()
            .flatMap(aggregate -> aggregate.methods.stream().map(method -> new AggregateMethodTemplateData(aggregate, method, valueObjectTypes)))
            .collect(Collectors.toList());
  }

  private AggregateMethodTemplateData(final Aggregate aggregate,
                                      final AggregateMethod aggregateMethod,
                                      final Map<String, List<Field>> valueObjectTypes) {
    this.templateParameters = TemplateParameters.with(AGGREGATE, aggregate).and(METHOD, aggregateMethod)
            .and(ROUTE, aggregate.routeTo(aggregateMethod)).and(FIELD_TYPES, aggregate.indexedStateFields)
            .and(VALUE_TYPES, valueObjectTypes).and(PACKAGE_NAME, resolvePackage(aggregate));
  }

  private String resolvePackage(final Aggregate aggregate) {
    final TemplateCustomFunctions customFunctions = TemplateCustomFunctions.instance();
    final String pluralAggregateName = customFunctions.makePlural(aggregate.aggregateName);
    return "src.components." +  customFunctions.decapitalize(pluralAggregateName);
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return ReactJsTemplateStandard.AGGREGATE_METHOD;
  }
}
