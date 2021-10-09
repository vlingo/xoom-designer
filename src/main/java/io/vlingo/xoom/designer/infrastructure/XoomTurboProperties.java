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

public class XoomTurboProperties {

  public static final String DOCKER_IMAGE = "docker.image";
  public static final String GLOO_UPSTREAM = "gloo.upstream";
  public static final String DOCKER_REPOSITORY = "docker.repository";

  private final Properties properties;

  static void resolve(final ApplicationDirectory applicationDirectory) {
    if (!ComponentRegistry.has(XoomTurboProperties.class)) {
      ComponentRegistry.register(XoomTurboProperties.class, new XoomTurboProperties(applicationDirectory));
    }
  }

  private XoomTurboProperties(final ApplicationDirectory applicationDirectory) {
    this.properties = Infrastructure.loadProperties(buildPath(applicationDirectory));
  }

  public static Properties properties() {
    if (!ComponentRegistry.has(XoomTurboProperties.class)) {
      throw new IllegalStateException("Xoom Properties are not available.");
    }
    return ComponentRegistry.withType(XoomTurboProperties.class).properties;
  }

  private Path buildPath(final ApplicationDirectory applicationDirectory) {
    final String subFolder = Profile.isTestProfileEnabled() ? "test" : "main";
    return Paths.get(applicationDirectory.path, "src", subFolder, "resources", "xoom-turbo.properties");
  }
}
