// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateProcessingStep;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final String appName = context.parameterOf(Label.APPLICATION_NAME);
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::of);
    final Boolean useAnnotations = context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf);
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    if(!useCQRS && !useAnnotations)
      return StorageTemplateDataFactory.build(basePackage, context.contents(), storageType, projectionType, appName, databases(context));

    return StorageTemplateDataFactory.buildWithCqrs(basePackage, context.contents(), storageType, projectionType, appName, databases(context));
  }

  private Map<Model, DatabaseType> databases(final CodeGenerationContext context) {
    if (context.parameterOf(Label.CQRS, Boolean::valueOf)) {
      return new HashMap<Model, DatabaseType>() {
        {
          put(Model.COMMAND, context.parameterOf(Label.COMMAND_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
          put(Model.QUERY, context.parameterOf(Label.QUERY_MODEL_DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
        }};
    }
    return new HashMap<Model, DatabaseType>() {
      {
        put(Model.DOMAIN, context.parameterOf(Label.DATABASE, name -> DatabaseType.getOrDefault(name, DatabaseType.IN_MEMORY)));
      }};
  }

  @Override
  protected Dialect resolveDialect(CodeGenerationContext context) {
    final String dialectName = dialectNameFrom(context);
    return dialectName.isEmpty() ? super.resolveDialect(context) : Dialect.withName(dialectName);
  }

  private String dialectNameFrom(CodeGenerationContext context) {
    return context.parameterOf(Label.DIALECT);
  }
}
