// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer;

import io.vlingo.xoom.designer.codegen.GenerationTarget;

import java.io.File;

public class ModelProcessingInformation {

  public final String userHomePath;
  public final String pathSeparator;
  public final String xoomDesignerVersion;
  public final String xoomDesignerFileVersion;
  public final GenerationTarget generationTarget;
  public final String generationTargetKey;

  public static ModelProcessingInformation from(final GenerationTarget generationTarget) {
    return new ModelProcessingInformation(generationTarget);
  }

  private ModelProcessingInformation(final GenerationTarget generationTarget) {
    this.pathSeparator = File.separator;
    this.generationTarget = generationTarget;
    this.generationTargetKey = generationTarget.value();
    this.userHomePath = System.getProperty("user.home");
    this.xoomDesignerVersion = Configuration.resolveDefaultXoomVersion();
    this.xoomDesignerFileVersion = Configuration.XOOM_DESIGNER_FILE_VERSION;
  }

}
