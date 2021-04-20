// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import java.io.File;

import io.vlingo.xoom.designer.Configuration;

import static io.vlingo.xoom.designer.Configuration.*;

public class ProjectGenerationInformation {

  public final String generationTarget;
  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerFileVersion;

  private static ProjectGenerationInformation instance = null;

  public static ProjectGenerationInformation load() {
    if(instance == null) {
      instance = new ProjectGenerationInformation();
    }
    return instance;
  }

  private ProjectGenerationInformation() {
    this.pathSeparator = File.separator;
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerFileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
    this.generationTarget = System.getProperty(XOOM_DESIGNER_GENERATION_TARGET, XOOM_DESIGNER_GENERATION_TARGET_FS);
  }

  public boolean requiresCompression() {
    return generationTarget.equalsIgnoreCase(XOOM_DESIGNER_GENERATION_TARGET_ZIP);
  }

  public static void reset() {
    instance = null;
  }
}
