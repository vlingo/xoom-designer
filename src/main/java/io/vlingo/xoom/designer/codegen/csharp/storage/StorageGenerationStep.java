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

import java.util.List;

public class StorageGenerationStep extends TemplateProcessingStep {

  @Override
  protected List<TemplateData> buildTemplatesData(final CodeGenerationContext context) {
    final String basePackage = context.parameterOf(Label.PACKAGE);
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final Boolean useAnnotations = context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf);
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    if(!useCQRS && !useAnnotations)
      return StorageTemplateDataFactory.build(basePackage, context.contents(), storageType, projectionType);

    return StorageTemplateDataFactory.buildWithCqrs(basePackage, context.contents(), storageType, projectionType);
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
