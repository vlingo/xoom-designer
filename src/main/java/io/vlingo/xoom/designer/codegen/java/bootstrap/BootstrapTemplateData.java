// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class BootstrapTemplateData extends TemplateData {

  private final static String PACKAGE_PATTERN = "%s.%s";
  private final static String INFRA_PACKAGE_NAME = "infrastructure";

  private final TemplateParameters parameters;
  private final static List<BootstrapTemplateData> TEMPLATES =
          Arrays.asList(new AnnotatedBootstrapTemplateData(), new DefaultBootstrapTemplateData());

  protected BootstrapTemplateData() {
    this.parameters = TemplateParameters.empty();
  }

  public static TemplateData from(final CodeGenerationContext context) {
    final Predicate<BootstrapTemplateData> supportCondition =
            templateData -> templateData.support(context);

    final BootstrapTemplateData bootstrapTemplateData =
            TEMPLATES.stream().filter(supportCondition).findFirst().get();

    bootstrapTemplateData.handleParameters(context);

    return bootstrapTemplateData;
  }

  private void handleParameters(final CodeGenerationContext context) {
    loadParameters(context);
    enrichParameters(context);
  }

  private TemplateParameters loadParameters(final CodeGenerationContext context) {
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    final String packageName = resolvePackage(context.parameterOf(Label.PACKAGE));
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::valueOf);
    final ProjectionType projectionType = context.parameterOf(Label.PROJECTION_TYPE, ProjectionType::valueOf);
    final Boolean hasExchange = ContentQuery.exists(JavaTemplateStandard.EXCHANGE_BOOTSTRAP, context.contents());

    final List<TypeRegistry> typeRegistries =
            TypeRegistry.from(storageType, useCQRS);

    final List<StoreProvider> storeProviders =
            StoreProvider.from(storageType, useCQRS, projectionType.isProjectionEnabled(), hasExchange);

    return this.parameters
            .and(TemplateParameter.PACKAGE_NAME, packageName)
            .and(TemplateParameter.PROVIDERS, storeProviders)
            .and(TemplateParameter.TYPE_REGISTRIES, typeRegistries)
            .and(TemplateParameter.USE_PROJECTIONS, projectionType.isProjectionEnabled())
            .and(TemplateParameter.APPLICATION_NAME, context.parameterOf(Label.APPLICATION_NAME))
            .and(TemplateParameter.USE_ANNOTATIONS, context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf))
            .andResolve(TemplateParameter.PROJECTION_DISPATCHER_PROVIDER_NAME, JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER::resolveClassname);
  }

  protected abstract void enrichParameters(final CodeGenerationContext context);

  protected abstract boolean support(final CodeGenerationContext context);

  protected String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, INFRA_PACKAGE_NAME).toLowerCase();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.BOOTSTRAP;
  }

}
