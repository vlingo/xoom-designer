// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.projectgeneration;

import io.vlingo.xoom.starter.Configuration;

public class ProjectGenerationInformation {

  public final String userHomePath;
  public final String settingsSchemaVersion;

  public static ProjectGenerationInformation load() {
    return new ProjectGenerationInformation();
  }

  private ProjectGenerationInformation() {
    this.userHomePath = System.getProperty("user.home");
    this.settingsSchemaVersion = Configuration.PROJECT_GENERATION_SETTINGS_SCHEMA_VERSION;
  }
}
