// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.gloo.steps;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandExecutionProcess;
import io.vlingo.xoom.designer.task.Property;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutionException;
import io.vlingo.xoom.designer.task.steps.CommandExecutionStep;

import java.util.Set;
import java.util.stream.Collectors;

public class GlooRouteCommandExecutionStep extends CommandExecutionStep {

    private static final String RESOURCE_PREFIX = "gloo.resource.";
    private static final String GATEWAY_PREFIX = "gloo.gateway.";
    private static final String COMMAND_PATTERN = "glooctl add route --path-exact /%s --dest-name %s --prefix-rewrite /%s";

  public GlooRouteCommandExecutionStep(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
    protected String formatCommands(final TaskExecutionContext context) {
        if(!context.hasProperty(Property.GLOO_UPSTREAM)) {
            throw new TaskExecutionException("Please set the Gloo upstream in vlingo-xoom.properties");
        }

        final Set<String> resources = filterResources(context);
        final String upstream = context.propertyOf(Property.GLOO_UPSTREAM);

        if(resources.isEmpty()) {
            throw new TaskExecutionException("Please check if the Gloo routes are properly mapped in vlingo-xoom.properties");
        }

        return resources.stream()
                .map(resource -> buildCommand(upstream, resource, context))
                .collect(Collectors.joining(" && "));
    }

    private String buildCommand(final String upstream, final String resource, final TaskExecutionContext context) {
        final String endpoint = context.propertyOf(RESOURCE_PREFIX + resource, value -> value);
        final String route = context.propertyOf(GATEWAY_PREFIX + resource, value -> value);
        return String.format(COMMAND_PATTERN, route, upstream, endpoint);
    }

    private Set<String> filterResources(final TaskExecutionContext context) {
        return context.properties().keySet().stream()
                .filter(key -> key.toString().startsWith(RESOURCE_PREFIX))
                .map(key -> key.toString().replace(RESOURCE_PREFIX, ""))
                .filter(resource -> context.properties().containsKey(GATEWAY_PREFIX + resource))
                .collect(Collectors.toSet());
    }

}
