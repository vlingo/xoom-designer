// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ComponentRegistry;

import java.nio.file.Paths;
import java.util.Properties;

public class DesignerProperties {
  private final Properties properties;
  public static final String SERVER_PORT = "designer.server.port";
  private static final String FILENAME = "xoom-designer.properties";

  static void resolve(final HomeDirectory homeDirectory) {
    if (!ComponentRegistry.has(DesignerProperties.class)) {
      ComponentRegistry.register(DesignerProperties.class, new DesignerProperties(homeDirectory));
    }
  }

  private DesignerProperties(final HomeDirectory homeDirectory) {
    this.properties = Infrastructure.loadProperties(Paths.get(homeDirectory.path, FILENAME));
  }

  public static int retrieveServerPort(final int defaultPort) {
    return ComponentRegistry.has(SERVER_PORT) ?
            Integer.valueOf(ComponentRegistry.withName(SERVER_PORT).toString()) :
            defaultPort;
  }

  public static Properties properties() {
    if (!ComponentRegistry.has(DesignerProperties.class)) {
      throw new IllegalStateException("Unresolved Designer Properties");
    }
    return ComponentRegistry.withType(DesignerProperties.class).properties;
  }
}
