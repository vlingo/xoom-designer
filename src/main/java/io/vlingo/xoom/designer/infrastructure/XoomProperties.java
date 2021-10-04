// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class XoomProperties {
  private final Properties properties;

  static void resolve(final ExternalDirectory externalDirectory) {
    if (!ComponentRegistry.has(XoomProperties.class)) {
      ComponentRegistry.register(XoomProperties.class, new XoomProperties(externalDirectory));
    }
  }

  private XoomProperties(final ExternalDirectory externalDirectory) {
    this.properties = Infrastructure.loadProperties(buildPath(externalDirectory));
  }

  public static Properties properties() {
    if (!ComponentRegistry.has(XoomProperties.class)) {
      throw new IllegalStateException("Xoom Properties are not available.");
    }
    return ComponentRegistry.withType(XoomProperties.class).properties;
  }

  private Path buildPath(final ExternalDirectory externalDirectory) {
    final String subFolder = Profile.isTestProfileEnabled() ? "test" : "main";
    return Paths.get(externalDirectory.path, "src", subFolder, "resources", "xoom-turbo.properties");
  }
}
