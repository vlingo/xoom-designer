// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.schemata;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateProcessingStep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class SchemataGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final List<CodeGenerationParameter> valueObjects =
            context.parametersOf(VALUE_OBJECT).collect(Collectors.toList());

    final List<CodeGenerationParameter> exchanges =
            context.parametersOf(AGGREGATE).flatMap(aggregate -> aggregate.retrieveAllRelated(EXCHANGE))
                    .collect(Collectors.toList());

    final List<CodeGenerationParameter> publishedValueObjects =
            findPublishedValueObjects(exchanges, valueObjects);

    final List<TemplateData> templateData = new ArrayList<>();
    templateData.addAll(DomainEventSpecificationTemplateData.from(exchanges));
    templateData.addAll(ValueObjectSpecificationTemplateData.from(exchanges, publishedValueObjects));
    templateData.add(new SchemataPluginTemplateData(exchanges, publishedValueObjects));
    return templateData;
  }

  private List<CodeGenerationParameter> findPublishedValueObjects(final List<CodeGenerationParameter> exchanges,
                                                                  final List<CodeGenerationParameter> valueObjects) {
    final List<CodeGenerationParameter> publishedValueObjects =
            ValueObjectDetail.findPublishedValueObjects(exchanges, valueObjects)
                    .collect(Collectors.toList());

    final List<CodeGenerationParameter> relatedValueObjects =
            publishedValueObjects.stream().flatMap(valueObject -> ValueObjectDetail.collectRelatedValueObjects(valueObject, valueObjects))
                    .collect(Collectors.toList());

    return Stream.of(publishedValueObjects, relatedValueObjects).flatMap(List::stream).collect(Collectors.toList());
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.parametersOf(AGGREGATE).flatMap(aggregate -> aggregate.retrieveAllRelated(EXCHANGE)).count() > 0;
  }
}
