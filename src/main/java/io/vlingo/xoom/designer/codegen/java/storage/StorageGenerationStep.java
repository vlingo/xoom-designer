// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final String appName = context.parameterOf(Label.APPLICATION_NAME);
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final Boolean useAnnotations = context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf);
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    final List<TemplateData> templatesData =
            StorageTemplateDataFactory.build(basePackage, appName, context.contents(), storageType,
                    databases(context), projectionType, useAnnotations, useCQRS);

    return filterConditionally(useAnnotations, templatesData);
  }

  private List<TemplateData> filterConditionally(final Boolean useAnnotations, final List<TemplateData> templatesData) {
    if (!useAnnotations) {
      return templatesData;
    }
    return templatesData.stream().filter(data -> !data.hasStandard(JavaTemplateStandard.ADAPTER)).collect(Collectors.toList());
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final String dialectName = context.parameters().retrieveValue(Label.DIALECT);
    return !dialectName.isEmpty() && Dialect.withName(dialectName).isJava() && context.parameterOf(Label.STORAGE_TYPE, StorageType::of).isEnabled();
  }

  private Map<Model, DatabaseType> databases(final CodeGenerationContext context) {
    if (context.parameterOf(Label.CQRS, Boolean::valueOf)) {
      return new HashMap<Model, DatabaseType>() {
        private static final long serialVersionUID = -3707004109115266302L;

      {
        put(Model.COMMAND, context.parameterOf(Label.COMMAND_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
        put(Model.QUERY, context.parameterOf(Label.QUERY_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
      }};
    }
    return new HashMap<Model, DatabaseType>() {
      private static final long serialVersionUID = 8419003086116028408L;

    {
      put(Model.DOMAIN, context.parameterOf(Label.DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
    }};
  }
}
