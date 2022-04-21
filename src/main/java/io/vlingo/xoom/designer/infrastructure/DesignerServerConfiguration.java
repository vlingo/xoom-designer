// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ComponentRegistry;

import java.net.MalformedURLException;
import java.net.URL;

public class DesignerServerConfiguration {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 19090;

  private final URL userInterfaceURL;
  private final int port;

  static DesignerServerConfiguration on(final Integer serverPort) {
    if (!ComponentRegistry.has(DesignerServerConfiguration.class)) {
      ComponentRegistry.register(DesignerServerConfiguration.class, new DesignerServerConfiguration(serverPort));
    }
    return ComponentRegistry.withType(DesignerServerConfiguration.class);
  }

  private DesignerServerConfiguration(final Integer serverPort) {
    try {
      this.port = serverPort > 0 ? serverPort : DEFAULT_PORT;
      this.userInterfaceURL = new URL(String.format("http://%s:%s/%s", DEFAULT_HOST, port, "platform"));
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }

  public int port() {
    if (!ComponentRegistry.has(DesignerServerConfiguration.class)) {
      throw new IllegalStateException("Designer Server is not available");
    }
    return ComponentRegistry.withType(DesignerServerConfiguration.class).port;
  }

  public URL resolveUserInterfaceURL() {
    if (!ComponentRegistry.has(DesignerServerConfiguration.class)) {
      throw new IllegalStateException("Designer Server is not available");
    }
    return ComponentRegistry.withType(DesignerServerConfiguration.class).userInterfaceURL;
  }

}
