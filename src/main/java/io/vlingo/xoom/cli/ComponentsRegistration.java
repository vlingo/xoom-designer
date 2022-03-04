// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.task.designer.DesignerTask;
import io.vlingo.xoom.cli.task.docker.DockerPackageTask;
import io.vlingo.xoom.cli.task.docker.DockerPushTask;
import io.vlingo.xoom.cli.task.docker.DockerStatusTask;
import io.vlingo.xoom.cli.task.gloo.GlooInitTask;
import io.vlingo.xoom.cli.task.gloo.GlooRouteTask;
import io.vlingo.xoom.cli.task.gloo.GlooSuspendTask;
import io.vlingo.xoom.cli.task.k8s.KubernetesPushTask;
import io.vlingo.xoom.cli.task.version.VersionDisplayTask;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Arrays;

public class ComponentsRegistration {

  public static void registerWith(final Logger logger,
                                  final CommandExecutionProcess commandExecutionProcess,
                                  final XoomTurboProperties properties) {
    
    ComponentRegistry.register(Logger.class, logger);
    
    ComponentRegistry.register(CommandExecutionProcess.class, commandExecutionProcess);
    
    ComponentRegistry.register("cliTasks", Arrays.asList(new DesignerTask(commandExecutionProcess),
                    new GlooInitTask(commandExecutionProcess), new GlooSuspendTask(commandExecutionProcess),
                    new GlooRouteTask(commandExecutionProcess, properties), new DockerPackageTask(commandExecutionProcess, properties),
                    new DockerPushTask(commandExecutionProcess, properties), new DockerStatusTask(commandExecutionProcess, properties),
                    new KubernetesPushTask(commandExecutionProcess), new VersionDisplayTask()));
  }

}
