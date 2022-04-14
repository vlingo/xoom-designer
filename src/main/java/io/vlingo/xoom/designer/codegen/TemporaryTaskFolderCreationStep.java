// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.cli.task.TaskExecutionException;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.designer.infrastructure.StagingFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemporaryTaskFolderCreationStep implements CodeGenerationStep {

  @Override
  public void process(final CodeGenerationContext context) {
    try {
      final Path temporaryTaskFolder =
              StagingFolder.path().resolve(context.generationId);

      Files.createDirectory(temporaryTaskFolder);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new TaskExecutionException(e);
    }
  }
}
