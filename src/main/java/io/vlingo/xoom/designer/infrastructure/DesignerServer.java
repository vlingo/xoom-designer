// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ComponentRegistry;

import java.net.MalformedURLException;
import java.net.URL;

public class DesignerServer {
  private static final int DEFAULT_SERVER_PORT = 19090;
  private static final String DEFAULT_SERVER_HOST = "localhost";
  private final URL url;

  static void resolve() {
    if (!ComponentRegistry.has(DesignerServer.class)) {
      ComponentRegistry.register(DesignerServer.class, new DesignerServer());
    }
  }

  private DesignerServer() {
    try {
      final int port = DesignerProperties.retrieveServerPort(DEFAULT_SERVER_PORT);
      this.url = new URL(String.format("http://%s:%s", DEFAULT_SERVER_HOST, port));
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }

  public static URL url() {
    if (!ComponentRegistry.has(DesignerServer.class)) {
      throw new IllegalStateException("Designer Server is not available");
    }
    return ComponentRegistry.withType(DesignerServer.class).url;
  }
}
