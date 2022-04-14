// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

public class TurboSettings {

  public final int httpServerPort;
  public final int localExchangePort;
  public final boolean useLocalExchange;

  public static TurboSettings with(final int httpServerPort,
                                   final int localExchangePort) {
    return new TurboSettings(httpServerPort, localExchangePort);
  }

  private TurboSettings(final int httpServerPort, final int localExchangePort) {
    this.httpServerPort = httpServerPort > 0 ? httpServerPort : 18080;
    this.localExchangePort = localExchangePort;
    this.useLocalExchange = localExchangePort > 0;
  }

}
