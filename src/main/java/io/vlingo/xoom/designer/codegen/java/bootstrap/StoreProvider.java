// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.storage.Model;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StoreProvider {

  private final String className;
  private final String arguments;

  public static List<StoreProvider> from(final StorageType storageType,
                                         final Boolean useCQRS,
                                         final Boolean useProjections,
                                         final Boolean hasExchange) {
    if (!storageType.isEnabled()) {
      return Collections.emptyList();
    }

    return Model.applicableTo(useCQRS)
            .map(model -> new StoreProvider(storageType, model, useProjections, hasExchange))
            .collect(toList());
  }

  private StoreProvider(final StorageType storageType,
                        final Model model,
                        final Boolean useProjections,
                        final Boolean hasExchange) {
    final TemplateParameters parameters =
            TemplateParameters.with(TemplateParameter.STORAGE_TYPE, storageType)
                    .and(TemplateParameter.MODEL, model);

    this.className = JavaTemplateStandard.STORE_PROVIDER.resolveClassname(parameters);
    this.arguments = resolveArguments(model, storageType, useProjections, hasExchange);
  }

  private String resolveArguments(final Model model,
                                  final StorageType storageType,
                                  final Boolean useProjections,
                                  final Boolean hasExchange) {
    final String typeRegistryObjectName =
            storageType.resolveTypeRegistryObjectName(model);

    final String exchangeDispatcherAccess =
            hasExchange ? "exchangeInitializer.dispatcher()" : "";

    final String projectionDispatcher =
            JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER.resolveClassname() + ".using(grid.world().stage()).storeDispatcher";

    final List<String> arguments =
            Stream.of("grid.world().stage()", typeRegistryObjectName).collect(toList());

    if (!model.isQueryModel()) {
      if (useProjections) {
        arguments.add(projectionDispatcher);
      }
      arguments.add(exchangeDispatcherAccess);
    }

    return arguments.stream().filter(arg -> !arg.isEmpty()).collect(joining(", "));
  }

  public String getClassName() {
    return className;
  }

  public String getArguments() {
    return arguments;
  }
}
