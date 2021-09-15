// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;

import java.util.List;
import java.util.stream.Collectors;

public class StorageProvider {

  private final String name;
  private final boolean useProjections;

  private StorageProvider(final TemplateParameters parameters) {
    final Model model =
            parameters.find(TemplateParameter.MODEL);

    final ProjectionType projectionType =
            parameters.find(TemplateParameter.PROJECTION_TYPE);

    this.name = parameters.find(TemplateParameter.STORE_PROVIDER_NAME);
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
