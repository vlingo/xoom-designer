// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ApplicationProperty;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.designer.cli.Property.DESIGNER_SERVER_PORT;

public class DesignerProperties {
  private final Properties properties;
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
    final String port =
            ComponentRegistry.has(DESIGNER_SERVER_PORT.literal()) ?
                    ComponentRegistry.withName(DESIGNER_SERVER_PORT.literal()).toString() :
                    ApplicationProperty.readValue(DESIGNER_SERVER_PORT.literal(), properties());

    return port == null ? defaultPort : Integer.valueOf(port);
  }

  public static Properties properties() {
    if (!ComponentRegistry.has(DesignerProperties.class)) {
      throw new IllegalStateException("Unresolved Designer Properties");
    }
    return ComponentRegistry.withType(DesignerProperties.class).properties;
  }
}
