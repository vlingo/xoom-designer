// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.designer.Configuration;

import java.util.ArrayList;
import java.util.List;

public class DesignerModel {

  public final PlatformSettingsData platformSettings;
  public final ContextSettingsData context;
  public final ModelSettingsData model;
  public final DeploymentSettingsData deployment;
  public final SchemataSettingsData schemata;
  public final String projectDirectory;
  public final Boolean useAnnotations;
  public final Boolean useAutoDispatch;
  public final String fileVersion;
  public final String generateUIWith;
  public final Boolean generateUI;

  public DesignerModel(PlatformSettingsData platformSettings, final ContextSettingsData context,
                       final ModelSettingsData model,
                       final DeploymentSettingsData deployment,
                       final SchemataSettingsData schemata,
                       final String projectDirectory,
                       final Boolean useAnnotations,
                       final Boolean useAutoDispatch,
                       final Boolean generateUI,
                       final String generateUIWith) {
    this.platformSettings = platformSettings;
    this.context = context;
    this.model = model;
    this.schemata = schemata;
    this.deployment = deployment;
    this.projectDirectory = projectDirectory;
    this.useAnnotations = useAnnotations;
    this.useAutoDispatch = useAutoDispatch;
    this.fileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
    this.generateUIWith = generateUIWith;
    this.generateUI = generateUI;
  }

  public List<String> validate() {
    final List<String> errorStrings = new ArrayList<>();
    if (context == null) {
      errorStrings.add("GenerationSettingsData.context is null");
    } else {
      context.validate(errorStrings);
    }
    if (model == null) {
      errorStrings.add("GenerationSettingsData.model is null");
    } else {
      model.validate(errorStrings);
    }
    if (deployment == null) {
      errorStrings.add("GenerationSettingsData.deployment is null");
    } else {
      deployment.validate(errorStrings);
    }
    if (projectDirectory == null) errorStrings.add("GenerationSettingsData.projectDirectory is null");
    if (useAnnotations == null) errorStrings.add("GenerationSettingsData.useAnnotations is null");
    if (useAutoDispatch == null) errorStrings.add("GenerationSettingsData.useAutoDispatch is null");
    return errorStrings;
  }

  public String defaultExchangeName() {
    return context.artifactId + "-exchange";
  }
}
