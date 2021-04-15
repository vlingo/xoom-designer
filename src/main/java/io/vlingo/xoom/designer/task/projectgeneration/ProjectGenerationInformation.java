// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import java.io.File;

import io.vlingo.xoom.designer.Configuration;

public class ProjectGenerationInformation {

  public final String generationTarget;
  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerFileVersion;

  public static ProjectGenerationInformation load() {
    return new ProjectGenerationInformation();
  }

  private ProjectGenerationInformation() {
    this.generationTarget = "filesystem"; // or: zip-download
    this.pathSeparator = File.separator;
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerFileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
  }
}
