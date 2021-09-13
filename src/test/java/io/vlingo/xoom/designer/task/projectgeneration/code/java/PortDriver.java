// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class PortDriver {

  public static PortDriver init() {
    if(System.getProperty("os.name").contains("Windows")) {
      return new WindowsPortDriver();
    }
    return new DefaultPortDriver();
  }

  public abstract void releasePort(final int port);

  public int findAvailable(final int from, final int to) {
    int port = from;
    while (port < to) {
      if (isPortAvailable(port)) {
        return port;
      } else {
        port++;
      }
    }
    throw new IllegalStateException("No open port in range " + from + " to " + to);
  }

  private boolean isPortAvailable(final int port) {
    try {
      new ServerSocket(port).close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private static class WindowsPortDriver extends PortDriver {

    @Override
    public void releasePort(int port) {
      //Runtime.getRuntime().exec("netstat -ano | findstr :<PORT>");
    }
  }

  private static class DefaultPortDriver extends PortDriver {

    @Override
    public void releasePort(int port) {

    }
  }

}
