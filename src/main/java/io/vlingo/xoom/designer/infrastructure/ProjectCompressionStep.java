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
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.IOException;

import static io.vlingo.xoom.cli.task.TaskOutput.COMPRESSED_PROJECT;

public class ProjectCompressionStep implements TaskExecutionStep {

  @Override
  public void processTaskWith(final TaskExecutionContext context) {
    try {
      context.addOutput(COMPRESSED_PROJECT, ProjectCompressor.compress(context.targetFolder()));
    } catch (final IOException e) {
      throw new TaskExecutionException(e);
    }
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return generationTarget().supportDownload();
  }

  private GenerationTarget generationTarget() {
    return ComponentRegistry.withType(GenerationTarget.class);
  }
}
