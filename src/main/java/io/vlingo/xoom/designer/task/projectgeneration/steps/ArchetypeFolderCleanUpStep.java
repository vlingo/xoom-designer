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
import io.vlingo.xoom.designer.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static io.vlingo.xoom.designer.Configuration.MAVEN_WRAPPER_DIRECTORY;

public class ArchetypeFolderCleanUpStep implements TaskExecutionStep {

    private static final List<Path> ALLOWED_FOLDERS =
            Arrays.asList(Paths.get(MAVEN_WRAPPER_DIRECTORY), Paths.get(Archetype.findDefault().label()));

    private static final Predicate<Path> LEFTOVERS =
            dir -> !ALLOWED_FOLDERS.contains(dir.getFileName());

    @Override
    public void process(final TaskExecutionContext context) {
        try {
            final Path archetypesFolder = ArchetypesFolder.path();
            Files.list(archetypesFolder).filter(Files::isDirectory)
                    .filter(LEFTOVERS).forEach(this::removeDirectory);
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

    private void removeDirectory(final Path directory) {
        try {
            FileUtils.deleteDirectory(directory.toFile());
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}