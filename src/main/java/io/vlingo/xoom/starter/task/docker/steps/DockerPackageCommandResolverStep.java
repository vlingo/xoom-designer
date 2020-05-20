// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.docker.steps;

import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import static io.vlingo.xoom.starter.task.Property.DOCKER_IMAGE;
import static io.vlingo.xoom.starter.task.option.OptionName.CURRENT_DIRECTORY;
import static io.vlingo.xoom.starter.task.option.OptionName.TAG;

public class DockerPackageCommandResolverStep extends CommandResolverStep {

    private static final String COMMAND_PATTERN = "%s && mvn clean package && docker build ./ -t %s:%s";

    @Override
    protected String formatCommands(final TaskExecutionContext context) {
        final String tag = context.optionValueOf(TAG);
        final String image = context.propertyOf(DOCKER_IMAGE);
        final String projectDirectoryCommand =
                resolveDirectoryChangeCommand(context.optionValueOf(CURRENT_DIRECTORY));

        if(image == null) {
            throw new DockerCommandException("Please set the docker.image property in vlingo-xoom.properties");
        }

        return String.format(COMMAND_PATTERN, projectDirectoryCommand, image, tag);
    }

}
