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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.vlingo.xoom.starter.Resource.ARCHETYPES_FOLDER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class ProjectInstallationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            final String artifactId = context.codeGenerationParameters().retrieveValue(Label.ARTIFACT_ID);
            final Path sourceDirectory = Paths.get(ARCHETYPES_FOLDER.path(), artifactId);
            Files.move(sourceDirectory, Paths.get(context.projectPath()), REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
