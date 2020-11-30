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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static io.vlingo.xoom.starter.Resource.ARCHETYPES_FOLDER;

public class ProjectInstallationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            final String artifactId = context.codeGenerationParameters().retrieveValue(Label.ARTIFACT_ID);
            final File sourceDirectory = Paths.get(ARCHETYPES_FOLDER.path(), artifactId).toFile();
            FileUtils.moveDirectory(sourceDirectory, new File(context.projectPath()));
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
