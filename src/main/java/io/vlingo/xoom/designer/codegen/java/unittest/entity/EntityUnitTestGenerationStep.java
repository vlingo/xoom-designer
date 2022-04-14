// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.entity;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUnitTestGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage =
            context.parameterOf(Label.PACKAGE);

    final ProjectionType projectionType =
            context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);

    final StorageType storageType =
            context.parameterOf(Label.STORAGE_TYPE, StorageType::valueOf);

    final List<CodeGenerationParameter> aggregates =
            context.parametersOf(Label.AGGREGATE).collect(Collectors.toList());

    final List<CodeGenerationParameter> valueObjects =
            context.parametersOf(Label.VALUE_OBJECT).collect(Collectors.toList());

    final List<Content> contents = context.contents();

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.add(new MockDispatcherTemplateData(basePackage, projectionType));
    templatesData.addAll(EntityUnitTestTemplateData.from(basePackage, storageType, projectionType, aggregates, valueObjects, contents));
    return templatesData;
  }

  @Override
  public boolean shouldProcess(CodeGenerationContext context) {
    final String dialectName = context.parameters().retrieveValue(Label.DIALECT);
    return !dialectName.isEmpty() && Dialect.withName(dialectName).isJava();
  }
}