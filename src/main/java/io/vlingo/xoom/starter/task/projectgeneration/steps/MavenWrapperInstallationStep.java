// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.infrastructure.Infrastructure.ArchetypesFolder;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.terminal.Terminal;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.Configuration.MAVEN_WRAPPER_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class MavenWrapperInstallationStep implements TaskExecutionStep {

    private static final List<String> MAVEN_WRAPPER_FILES = Arrays.asList("mvnw", "mvnw.cmd");

    @Override
    public void process(final TaskExecutionContext context) {
        final Path projectPath = Paths.get(context.projectPath());
        copyMavenWrapperFiles(projectPath);
        copyMavenWrapperDirectory(projectPath);
    }

    private void copyMavenWrapperFiles(final Path projectPath) {
        MAVEN_WRAPPER_FILES.forEach(filename -> {
            try {
                final Path source = ArchetypesFolder.path().resolve(filename);
                final Path destination = projectPath.resolve(filename);
                Files.copy(source, destination, REPLACE_EXISTING);
                final File file = destination.toFile();
                Terminal.grantAllPermissions(file);
            } catch (final IOException e) {
                throw new ProjectGenerationException(e);
            }
        });
    }

    private void copyMavenWrapperDirectory(final Path projectPath) {
        try {
            final Path source = ArchetypesFolder.path().resolve(MAVEN_WRAPPER_DIRECTORY);
            final Path destination = projectPath.resolve(MAVEN_WRAPPER_DIRECTORY);
            FileUtils.copyDirectory(source.toFile(), destination.toFile());
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
