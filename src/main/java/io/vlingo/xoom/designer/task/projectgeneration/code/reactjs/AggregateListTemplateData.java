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
import io.vlingo.xoom.designer.task.projectgeneration.code.java.Field;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.TemplateParameter.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.reactjs.TemplateParameter.VALUE_TYPES;

public class AggregateListTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public static List<TemplateData> from(final List<Aggregate> aggregates,
                                        final Map<String, List<Field>> valueObjectTypes) {
    return aggregates.stream().map(aggregate -> new AggregateListTemplateData(aggregate, valueObjectTypes)).collect(Collectors.toList());
  }

  private AggregateListTemplateData(final Aggregate aggregate,
                                   final Map<String, List<Field>> valueObjectTypes) {
    templateParameters = TemplateParameters.with(AGGREGATE, aggregate)
            .and(VALUE_TYPES, valueObjectTypes)
            .and(PACKAGE_NAME, resolvePackage(aggregate));
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
    return ReactJsTemplateStandard.AGGREGATE_LIST;
  }
}
