// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class ContentPurgerStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        ContentAvailability.availabilities().forEach(contentAvailability -> {
            if(!contentAvailability.shouldBeAvailable(context)) {
                final String contentPath =
                        contentAvailability.resolvePath(context);

                purge(new File(contentPath));
            }
        });
    }

    private void purge(final File content) {
        if(content.isFile()) {
            purgeFile(content);
        } else {
            purgeDirectory(content);
        }
    }

    private void purgeFile(final File content) {
        content.delete();
    }

    private void purgeDirectory(final File content){
        try {
            Files.walk(content.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
