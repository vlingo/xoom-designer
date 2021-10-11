// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.task.TaskExecutionException;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;

import java.util.Set;
import java.util.stream.Collectors;

class GlooRouteCommandExecutor extends CommandExecutor {

  private static final String RESOURCE_PREFIX = "gloo.resource.";
  private static final String GATEWAY_PREFIX = "gloo.gateway.";
  private static final String COMMAND_PATTERN = "glooctl add route --path-exact /%s --dest-name %s --prefix-rewrite /%s";

  private final XoomTurboProperties xoomTurboProperties;

  protected GlooRouteCommandExecutor(final CommandExecutionProcess commandExecutionProcess,
                                     final XoomTurboProperties xoomTurboProperties) {
    super(commandExecutionProcess);
    this.xoomTurboProperties = xoomTurboProperties;
  }

  @Override
  protected String formatCommands() {
    if (!xoomTurboProperties.hasProperty(XoomTurboProperties.GLOO_UPSTREAM)) {
      throw new TaskExecutionException("Please set the Gloo upstream in xoom-turbo.properties");
    }

    final Set<String> resources = filterResources();
    final String upstream = xoomTurboProperties.get(XoomTurboProperties.GLOO_UPSTREAM);

    if (resources.isEmpty()) {
      throw new TaskExecutionException("Please check if the Gloo routes are properly mapped in xoom-turbo.properties");
    }

    return resources.stream()
            .map(resource -> buildCommand(upstream, resource))
            .collect(Collectors.joining(" && "));
  }

  private String buildCommand(final String upstream, final String resource) {
    final String endpoint = xoomTurboProperties.get(RESOURCE_PREFIX + resource);
    final String route = xoomTurboProperties.get(GATEWAY_PREFIX + resource);
    return String.format(COMMAND_PATTERN, route, upstream, endpoint);
  }

  private Set<String> filterResources() {
    return xoomTurboProperties.filterProperties(entry -> entry.getKey().toString().startsWith(RESOURCE_PREFIX))
            .map(entry -> entry.getKey().toString().replace(RESOURCE_PREFIX, ""))
            .filter(resource -> xoomTurboProperties.hasProperty(GATEWAY_PREFIX + resource))
            .collect(Collectors.toSet());
  }
}
