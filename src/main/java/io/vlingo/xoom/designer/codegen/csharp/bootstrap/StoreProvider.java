// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.bootstrap;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.storage.Model;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StoreProvider {

  private final String className;
  private final String arguments;

  public static List<StoreProvider> from(final StorageType storageType, final Boolean useCQRS,
                                         final Boolean useProjections) {
    if (!storageType.isEnabled()) {
      return Collections.emptyList();
    }

    return (useCQRS ? Model.applicableToQueryAndCommand() : Model.applicableToDomain())
        .map(model -> new StoreProvider(storageType, model, useProjections))
        .collect(toList());
  }

  private StoreProvider(final StorageType storageType, final Model model, final Boolean useProjections) {
    final TemplateParameters parameters = TemplateParameters.with(TemplateParameter.STORAGE_TYPE, storageType)
        .and(TemplateParameter.MODEL, model);

    this.className = CsharpTemplateStandard.STORE_PROVIDER.resolveClassname(parameters);
    this.arguments = resolveArguments(model, storageType, useProjections);
  }

  private String resolveArguments(final Model model, final StorageType storageType, final Boolean useProjections) {
    final String typeRegistryObjectName = storageType.resolveTypeRegistryObjectName(model);

    final String exchangeDispatcherAccess = "";

    final String projectionDispatcher =
        CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER.resolveClassname() + ".Using(World.Stage).StoreDispatcher";

    final List<String> arguments = Stream.of("World.Stage", typeRegistryObjectName).collect(toList());

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
