// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import static io.vlingo.xoom.starter.Resource.ARCHETYPES_FOLDER;

public final class ProjectInstallationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            final String artifactId = context.codeGenerationParameters().retrieveValue(Label.ARTIFACT_ID);
            final Path source = Paths.get(ARCHETYPES_FOLDER.path(), artifactId);
            final Path destination = Paths.get(context.projectPath());
            FileUtils.moveDirectory(source.toFile(), destination.toFile());
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
