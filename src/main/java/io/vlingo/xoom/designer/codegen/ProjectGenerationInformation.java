// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.designer.ComponentsConfiguration;

import java.io.File;

public class ProjectGenerationInformation {

  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerVersion;
  public final String xoomDesignerFileVersion;
  public final GenerationTarget generationTarget;
  public final String generationTargetKey;

  public static ProjectGenerationInformation from(final GenerationTarget generationTarget) {
    return new ProjectGenerationInformation(generationTarget);
  }

  private ProjectGenerationInformation(final GenerationTarget generationTarget) {
    this.pathSeparator = File.separator;
    this.generationTarget = generationTarget;
    this.generationTargetKey = generationTarget.value();
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerVersion = ComponentsConfiguration.resolveDefaultXoomVersion();
    this.xoomDesignerFileVersion = ComponentsConfiguration.XOOM_DESIGNER_FILE_VERSION;
  }

}
