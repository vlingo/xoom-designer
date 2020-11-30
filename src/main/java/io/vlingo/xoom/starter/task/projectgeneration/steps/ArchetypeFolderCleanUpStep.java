// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.starter.task.projectgeneration.archetype.Archetype;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.starter.Configuration.MAVEN_WRAPPER_DIRECTORY;
import static io.vlingo.xoom.starter.Resource.ARCHETYPES_FOLDER;
import static java.util.Arrays.asList;

public class ArchetypeFolderCleanUpStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final Archetype defaultArchetype = Archetype.findDefault();
        final List<String> expectedExistingFolders = asList(MAVEN_WRAPPER_DIRECTORY, defaultArchetype.label());
        final File archetypeFolder = Paths.get(ARCHETYPES_FOLDER.path()).toFile();
        Stream.of(archetypeFolder.listFiles(File::isDirectory))
                .filter(dir -> !expectedExistingFolders.contains(dir.getName()))
                .forEach(this::removeRemainingDirectory);
    }

    private void removeRemainingDirectory(final File directory) {
        try {
            FileUtils.deleteDirectory(directory);
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
