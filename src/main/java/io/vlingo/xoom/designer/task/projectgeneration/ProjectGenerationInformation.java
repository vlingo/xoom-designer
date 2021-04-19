// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;

import java.io.File;

import io.vlingo.xoom.designer.Configuration;

public class ProjectGenerationInformation {

  public final String generationTarget;
  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerFileVersion;
  public final boolean angularCLI;

  public static ProjectGenerationInformation load() {
    return new ProjectGenerationInformation();
  }

  private ProjectGenerationInformation() {
    this.generationTarget = System.getProperty(Configuration.XOOM_DESIGNER_GENERATION_TARGET, Configuration.XOOM_DESIGNER_GENERATION_TARGET_FS);
    this.pathSeparator = File.separator;
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerFileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
    this.angularCLI = Infrastructure.AngularCLI.isInstalled();
  }
}
