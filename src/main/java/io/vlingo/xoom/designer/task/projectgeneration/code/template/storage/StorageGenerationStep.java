// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.turbo.codegen.CodeGenerationContext;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateProcessingStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.DatabaseType.IN_MEMORY;
import static io.vlingo.xoom.turbo.annotation.codegen.template.AnnotationBasedTemplateStandard.ADAPTER;

public class StorageGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(PACKAGE);
    final String appName = context.parameterOf(APPLICATION_NAME);
    final StorageType storageType = context.parameterOf(STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(PROJECTION_TYPE, ProjectionType::valueOf);
    final Boolean useAnnotations = context.parameterOf(USE_ANNOTATIONS, Boolean::valueOf);
    final Boolean useCQRS = context.parameterOf(CQRS, Boolean::valueOf);
    final List<TemplateData> templatesData =
            StorageTemplateDataFactory.build(basePackage, appName, context.contents(), storageType,
                    databases(context), projectionType, useAnnotations, useCQRS);

    return filterConditionally(useAnnotations, templatesData);
  }

  private List<TemplateData> filterConditionally(final Boolean useAnnotations, final List<TemplateData> templatesData) {
    if (!useAnnotations) {
      return templatesData;
    }
    return templatesData.stream().filter(data -> !data.hasStandard(ADAPTER)).collect(Collectors.toList());
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    return context.parameterOf(STORAGE_TYPE, StorageType::of).isEnabled();
  }

  private Map<Model, DatabaseType> databases(final CodeGenerationContext context) {
    if (context.parameterOf(CQRS, Boolean::valueOf)) {
      return new HashMap<Model, DatabaseType>() {{
        put(Model.COMMAND, context.parameterOf(COMMAND_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, IN_MEMORY)));
        put(Model.QUERY, context.parameterOf(QUERY_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, IN_MEMORY)));
      }};
    }
    return new HashMap<Model, DatabaseType>() {{
      put(Model.DOMAIN, context.parameterOf(DATABASE, name -> DatabaseType.getOrDefault(name, IN_MEMORY)));
    }};
  }
}
