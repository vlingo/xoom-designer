// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import java.io.IOException;
import java.net.ServerSocket;

public class AvailablePort {

  public static int find(final int from, final int to) {
    return new AvailablePort().onRange(from, to);
  }

  private int onRange(final int from, final int to) {
    int port = from;
    while (port < to) {
      if (isPortFree(port)) {
        return port;
      } else {
        port++;
      }
    }
    throw new IllegalStateException("No open port in range " + from + " to " + to);
  }

  private boolean isPortFree(final int port) {
    try {
      new ServerSocket(port).close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

}
