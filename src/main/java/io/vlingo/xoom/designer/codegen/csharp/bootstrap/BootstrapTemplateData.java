// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BootstrapTemplateData extends TemplateData {

  private final static String PACKAGE_PATTERN = "%s.%s";
  private final static String INFRA_PACKAGE_NAME = "Infrastructure";

  private final TemplateParameters parameters;

  protected BootstrapTemplateData() {
    this.parameters = TemplateParameters.empty();
  }

  public static TemplateData from(final CodeGenerationContext context) {
    final Predicate<BootstrapTemplateData> supportCondition = templateData -> templateData.support(context);

    final BootstrapTemplateData bootstrapTemplateData = Stream.of(new DefaultBootstrapTemplateData())
        .filter(supportCondition).findFirst().get();

    bootstrapTemplateData.handleParameters(context);

    return bootstrapTemplateData;
  }

  private void handleParameters(final CodeGenerationContext context) {
    loadParameters(context);
    enrichParameters(context);
  }

  private TemplateParameters loadParameters(final CodeGenerationContext context) {
    final String packageName = resolvePackage(context.parameterOf(Label.PACKAGE));
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::of);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::of);

    final List<TypeRegistry> typeRegistries = TypeRegistry.from(storageType, useCQRS);
    final List<StoreProvider> storeProviders = StoreProvider.from(storageType, useCQRS, projectionType.isProjectionEnabled());
    final Set<String> qualifiedNames = ContentQuery.findFullyQualifiedClassNames(context.contents(), CsharpTemplateStandard.STORE_PROVIDER,
        CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, CsharpTemplateStandard.REST_RESOURCE);

    return this.parameters
        .and(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.APPLICATION_NAME, context.parameterOf(Label.APPLICATION_NAME))
        .and(TemplateParameter.PROVIDERS, storeProviders)
        .and(TemplateParameter.TYPE_REGISTRIES, typeRegistries)
        .and(TemplateParameter.REST_RESOURCES, RestResource.from(context.contents()))
        .addImports(qualifiedNames);
  }

  protected abstract void enrichParameters(final CodeGenerationContext context);

  protected abstract boolean support(final CodeGenerationContext context);

  protected String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, INFRA_PACKAGE_NAME);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.BOOTSTRAP;
  }

}
