// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.cli.CommandExecutionStep;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.infrastructure.terminal.ObservableCommandExecutionProcess;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class DockerServices extends CommandExecutionStep {

  private final PortDriver portDriver;
  private final CommandObserver observer;

  public static final String SCHEMATA = "schemata";
  public static final String RABBIT_MQ = "rabbitmq";

  public static void run() {
    if(isSupported()) {
      if (!ComponentRegistry.has(DockerServices.class)) {
        new DockerServices(new CommandObserver()).start();
      }
      if (ComponentRegistry.withType(DockerServices.class).isSucceeded()) {
        Logger.basicLogger().info("Docker services running...");
      } else {
        Logger.basicLogger().warn("Unable to run Docker Services");
      }
    }
  }

  public static void shutdown() {
    if(isSupported()) {
      if (ComponentRegistry.has(DockerServices.class)) {
        Logger.basicLogger().info("Stopping Docker services...");
        ComponentRegistry.withType(DockerServices.class).stop();
      }
    }
  }

  private DockerServices(final CommandObserver observer) {
    super(new ObservableCommandExecutionProcess(observer));
    ComponentRegistry.register(DockerServices.class, this);
    this.observer = observer;
    this.portDriver = PortDriver.init();
  }

  private void start() {
      process();
  }

  private void stop() {
    if(!isStopped()) {
      process();
      observer.stop();
    }
  }

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final String directoryChangeCommand =
            Terminal.supported().resolveDirectoryChangeCommand(ProjectGenerationTest.e2eResourcesPath);

    return isNew() ? resolveStartUpCommand(directoryChangeCommand) : resolveShutdownCommand(directoryChangeCommand);
  }

  private String resolveStartUpCommand(final String directoryChangeCommand) {
    return String.format("%s && docker-compose up -d", directoryChangeCommand);
  }

  private String resolveShutdownCommand(final String directoryChangeCommand) {
    return String.format("%s && docker-compose stop", directoryChangeCommand);
  }

  private boolean isSucceeded() {
    return observer.status.equals(ExecutionStatus.SUCCEEDED);
  }

  private boolean isStopped() {
    return observer.status.equals(ExecutionStatus.STOPPED);
  }

  private boolean isNew() {
    return observer.status.equals(ExecutionStatus.NEW);
  }

  private static boolean isSupported() {
    return Boolean.valueOf(System.getProperty("auto-deps-initialization", "false"));
  }

  public static int findPortOf(final String serviceName) {
    final String port = System.getProperty(serviceName + "-port");
    if(port == null) {
      throw new IllegalArgumentException("Unknown port for service " + serviceName);
    }
    return Integer.valueOf(port);
  }
}
