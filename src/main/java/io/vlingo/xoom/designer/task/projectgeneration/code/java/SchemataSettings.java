// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

public class SchemataSettings {

  public final String host;
  public final int port;

  public static SchemataSettings with(final String host, final int port) {
    return new SchemataSettings(host, port);
  }

  private SchemataSettings(final String host, final int port) {
    this.host = host;
    this.port = port;
  }
}
