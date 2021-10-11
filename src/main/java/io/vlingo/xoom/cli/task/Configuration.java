// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.cli.task.designer.DesignerTask;
import io.vlingo.xoom.cli.task.docker.DockerPackageTask;
import io.vlingo.xoom.cli.task.docker.DockerPushTask;
import io.vlingo.xoom.cli.task.docker.DockerStatusTask;
import io.vlingo.xoom.cli.task.gloo.GlooInitTask;
import io.vlingo.xoom.cli.task.gloo.GlooRouteTask;
import io.vlingo.xoom.cli.task.gloo.GlooSuspendTask;
import io.vlingo.xoom.cli.task.k8s.KubernetesPushTask;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Arrays;

public class Configuration {

  public static void init(final XoomTurboProperties properties) {
    ComponentRegistry.register("cliTasks",
            Arrays.asList(new DesignerTask(ComponentRegistry.withType(CommandExecutionProcess.class)),
                    new GlooInitTask(ComponentRegistry.withType(CommandExecutionProcess.class)),
                    new GlooSuspendTask(ComponentRegistry.withType(CommandExecutionProcess.class)),
                    new GlooRouteTask(ComponentRegistry.withType(CommandExecutionProcess.class), properties),
                    new DockerPackageTask(ComponentRegistry.withType(CommandExecutionProcess.class), properties),
                    new DockerPushTask(ComponentRegistry.withType(CommandExecutionProcess.class), properties),
                    new DockerStatusTask(ComponentRegistry.withType(CommandExecutionProcess.class), properties),
                    new KubernetesPushTask(ComponentRegistry.withType(CommandExecutionProcess.class), properties)));
  }
}
