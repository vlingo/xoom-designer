// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java;

import io.vlingo.xoom.common.Tuple2;

import java.util.Optional;

public class SchemataSettings {

  public final String host;
  public final int port;
  public final Optional<Tuple2<String,Integer>> serviceDNS;

  public static SchemataSettings with(final String host,
                                      final int port,
                                      final Optional<Tuple2<String,Integer>> serviceDNS) {
    return new SchemataSettings(host, port, serviceDNS);
  }

  private SchemataSettings(final String host,
                           final int port,
                           final Optional<Tuple2<String,Integer>> serviceDNS) {
    this.host = host;
    this.port = port;
    this.serviceDNS = serviceDNS;
  }
}
