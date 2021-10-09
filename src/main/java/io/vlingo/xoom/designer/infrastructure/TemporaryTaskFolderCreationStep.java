// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionException;
import io.vlingo.xoom.cli.task.TaskExecutionStep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemporaryTaskFolderCreationStep implements TaskExecutionStep {

  @Override
  public void processTaskWith(final TaskExecutionContext context) {
    try {
      final Path temporaryTaskFolder =
              StagingFolder.path().resolve(context.executionId);

      Files.createDirectory(temporaryTaskFolder);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new TaskExecutionException(e);
    }
  }
}
