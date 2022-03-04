// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.terminal.CommandOutputConsumer;
import io.vlingo.xoom.terminal.Terminal;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Runtime.getRuntime;

public abstract class PortDriver {

  protected final Terminal terminal = Terminal.supported();

  public static PortDriver init() {
    if(Terminal.supported().isWindows()) {
      return new WindowsPortDriver();
    }
    return new UnixPortDriver();
  }

  public abstract boolean release(final int port);

  public int findAvailable() {
    return findAvailable(19099, 20100);
  }

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

  public boolean isPortAvailable(final int port,
                                 final int interval,
                                 final int confirmationRetries,
                                 final boolean skipRetriesOnStatus) {
    int retry = 0;
    boolean available;
    do {
      retry++;
      available = isPortAvailable(port);
      wait(interval);
    } while (retry < confirmationRetries && available != skipRetriesOnStatus);
    return available;
  }

  public boolean isPortAvailable(final int port) {
    try {
      new ServerSocket(port).close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private void wait(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (final InterruptedException exception) {
      Logger.basicLogger().warn("Unable to wait " + milliseconds + " ms", exception);
    }
  }

  private static class UnixPortDriver extends PortDriver {

    @Override
    public boolean release(int port) {
      try {
        final String command = String.format("fuser -n tcp -k %s", port);
        getRuntime().exec(terminal.prepareCommand(command)).waitFor();
      } catch (final IOException | InterruptedException exception) {
        exception.printStackTrace();
      }
      return isPortAvailable(port, 5, 300, true);
    }
  }

  private static class WindowsPortDriver extends PortDriver {

    private final Map<Integer, Integer> processIds = new HashMap<>();

    @Override
    public boolean release(int port) {
      try {
        registerPID(port);
        killProcessOn(port);
      } catch (final IOException | InterruptedException exception) {
        exception.printStackTrace();
      }

      if(isPortAvailable(port, 5, 300, true)) {
        processIds.remove(port);
        return true;
      }
      return false;
    }

    private void registerPID(final int port) throws IOException, InterruptedException {
      final String command = String.format("netstat -ano | findstr :%s", port);
      final Process process = getRuntime().exec(terminal.prepareCommand(command));
      registerPID(process, port);
      process.waitFor();
    }

    private void killProcessOn(final int port) throws IOException, InterruptedException {
      final String command = String.format("taskkill /PID %s /F", processIds.get(port));
      getRuntime().exec(terminal.prepareCommand(command)).waitFor();
    }

    private void registerPID(final Process process, final int port) {
      CommandOutputConsumer.of(process).consumeWith(output -> {
       if(output.trim().startsWith("TCP") && !processIds.containsKey(port)) {
         final String[] relevantBlocks = output.split("[^\\d]+");
         final String pid = relevantBlocks[relevantBlocks.length - 1];
         processIds.put(port, Integer.parseInt(pid));
       }
      });
    }
  }

}
