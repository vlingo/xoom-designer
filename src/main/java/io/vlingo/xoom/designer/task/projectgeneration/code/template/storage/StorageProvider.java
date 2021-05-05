// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.turbo.codegen.template.TemplateParameter.*;

public class StorageProvider {

  private final String name;
  private final boolean useProjections;

  private StorageProvider(final TemplateParameters parameters) {
    final Model model =
            parameters.find(MODEL);

    final ProjectionType projectionType =
            parameters.find(PROJECTION_TYPE);

    this.name = parameters.find(STORE_PROVIDER_NAME);
    this.useProjections = projectionType.isProjectionEnabled() && model.isCommandModel();
  }

  public static List<StorageProvider> from(final List<TemplateData> templateData) {
    return templateData.stream()
            .map(data -> new StorageProvider(data.parameters()))
            .collect(Collectors.toList());
  }

  public String getName() {
    return name;
  }

  public boolean getUseProjections() {
    return useProjections;
  }

}
