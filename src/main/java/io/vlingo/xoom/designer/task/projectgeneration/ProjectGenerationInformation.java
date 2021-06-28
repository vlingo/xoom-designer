// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.Configuration;

import java.io.File;

public class ProjectGenerationInformation {

  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerFileVersion;
  public final GenerationTarget generationTarget;
  public final String generationTargetKey;

  public static ProjectGenerationInformation from(final GenerationTarget generationTarget) {
    return new ProjectGenerationInformation(generationTarget);
  }

  private ProjectGenerationInformation(final GenerationTarget generationTarget) {
    this.pathSeparator = File.separator;
    this.generationTarget = generationTarget;
    this.generationTargetKey = generationTarget.key();
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerFileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
  }

}
