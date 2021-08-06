// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import java.util.Optional;

public class SchemataSettings {

  public final String host;
  public final int port;
  public final Optional<String> serviceName;

  public static SchemataSettings with(final String host,
                                      final int port,
                                      final Optional<String> serviceName) {
    return new SchemataSettings(host, port, serviceName);
  }

  private SchemataSettings(final String host,
                           final int port,
                           final Optional<String> serviceName) {
    this.host = host;
    this.port = port;
    this.serviceName = serviceName;
  }
}
