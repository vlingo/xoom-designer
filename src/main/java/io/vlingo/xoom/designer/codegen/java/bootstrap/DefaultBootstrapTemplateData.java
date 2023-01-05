// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.Set;

public class DefaultBootstrapTemplateData extends BootstrapTemplateData {

  @Override
  protected void enrichParameters(final CodeGenerationContext context) {
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);
    final StorageType storageType = context.parameterOf(Label.STORAGE_TYPE, StorageType::valueOf);

    final Set<String> qualifiedNames =
            ContentQuery.findFullyQualifiedClassNames(context.contents(),
                    JavaTemplateStandard.STORE_PROVIDER, JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, JavaTemplateStandard.REST_RESOURCE, JavaTemplateStandard.REST_UI_RESOURCE,
                    JavaTemplateStandard.AUTO_DISPATCH_RESOURCE_HANDLER, JavaTemplateStandard.AUTO_DISPATCH_RESOURCE_UI_HANDLER, JavaTemplateStandard.EXCHANGE_BOOTSTRAP);

    parameters().and(TemplateParameter.REST_RESOURCES, RestResource.from(context.contents()))
            .and(TemplateParameter.EXCHANGE_BOOTSTRAP_NAME, JavaTemplateStandard.EXCHANGE_BOOTSTRAP.resolveClassname())
            .and(TemplateParameter.HAS_EXCHANGE, ContentQuery.exists(JavaTemplateStandard.EXCHANGE_BOOTSTRAP, context.contents()))
            .addImports(qualifiedNames).addImports(storageType.resolveTypeRegistryQualifiedNames(useCQRS));
  }

  @Override
  protected boolean support(final CodeGenerationContext context) {
    return !context.parameterOf(Label.USE_ANNOTATIONS, Boolean::valueOf);
  }

}
