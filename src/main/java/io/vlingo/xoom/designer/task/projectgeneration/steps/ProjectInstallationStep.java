// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.vlingo.xoom.turbo.codegen.parameter.Label.ARTIFACT_ID;

public final class ProjectInstallationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String artifactId = context.codeGenerationParameters().retrieveValue(ARTIFACT_ID);
        final Path source = ArchetypesFolder.path().resolve(artifactId);
        final Path destination = Paths.get(context.projectPath());
        this.installProject(source.toFile(), destination.toFile());
    }

    private void installProject(final File source, final File destination) {
        try {
            createParentFolders(destination);
            FileUtils.copyDirectory(source, destination);
        } catch (IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

    private void createParentFolders(final File destination) {
        final File parentFolders = destination.toPath().getParent().toFile();
        if(!parentFolders.exists() && !parentFolders.mkdirs()) {
            throw new ProjectGenerationException("Unable to create project parent folder(s)");
        }
    }

}
